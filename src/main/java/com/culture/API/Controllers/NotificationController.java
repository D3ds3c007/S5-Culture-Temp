package com.culture.API.Controllers;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culture.API.Models.Field;
import com.culture.API.Models.GroundType;
import com.culture.API.Models.Owner;
import com.culture.API.Models.Plot;
import com.culture.API.Models.DTO.OwnerDTO;
import com.culture.API.Models.MongodbEntity.FieldLocalisation;
import com.culture.API.Models.MongodbEntity.FieldPictures;
import com.culture.API.Models.MongodbEntity.Notification;
import com.culture.API.Models.MongodbEntity.PendingField;
import com.culture.API.Models.Request.AddFieldRequest;
import com.culture.API.Repository.FieldRepository;
import com.culture.API.Repository.GroundTypeRepository;
import com.culture.API.Repository.NotificationRepository;
import com.culture.API.Repository.OwnerRepository;
import com.culture.API.Repository.PendingFieldRepository;
import com.culture.API.Repository.PlotRepository;
import com.culture.API.Repository.FieldLocalisationRepository;
import com.culture.API.Repository.FieldPicturesRepository;
import com.culture.API.Utils.HashGenerator;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "*", methods= {RequestMethod.POST, RequestMethod.GET})
@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private FieldRepository fieldrepository;
    
    @Autowired
    private PendingFieldRepository pendingRepository;

    @Autowired
    private FieldPicturesRepository picturesdRepository;

    @Autowired
    private FieldLocalisationRepository localisationRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private GroundTypeRepository groundTypeRepository;

    @Autowired
    private PlotRepository plotRepository;

    @GetMapping("/notifications")
    public  ResponseEntity<List<Notification>> getAllNotification() {
        try {
            List<Notification> notifs = Notification.findAll(repository); 
            return new ResponseEntity<>(notifs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/notification")
    public ResponseEntity<Notification> insertNotif(@RequestBody AddFieldRequest request) {

      

        String hashcode = HashGenerator.generateCode();


        try {
            Owner o = Owner.findOwnerById(request.getIdOwner(), ownerRepository);

            OwnerDTO oDto = new OwnerDTO(o.getIdOwner(), o.getName(), o.getEmail());

            PendingField p = new PendingField();
                p.setArea(request.getArea());
                p.setOwner(oDto);
                p.setDescription(request.getDescription());
                p.setLocation(request.getLocation());
                p.setHashcode(hashcode);
                p.setGroundType(request.getGroundType());
                p.setPlotNumber(Integer.parseInt(request.getPlotNumber()));
                pendingRepository.save(p);

            FieldPictures fp = new FieldPictures();
                fp.setHashcode(hashcode);
                fp.setPicBase64(request.getPictures()[0]);

            FieldPictures  fps= fp.save(fp, picturesdRepository);

            for (int i = 0; i < request.getLocalisation().length; i++) {
                FieldLocalisation l = new FieldLocalisation();
                    l.setHashcode(hashcode);
                    l.setLatitude(request.getLocalisation()[i].getPosition().getLat());
                    l.setLongitude(request.getLocalisation()[i].getPosition().getLng());
                    localisationRepository.save(l);
            }

            Notification notify = new Notification();
              notify.setHashcode(hashcode);
              notify.setOwner(oDto);
              notify.setDate(new Timestamp(System.currentTimeMillis()));

            Notification n = Notification.save(notify, repository);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate")
    public  ResponseEntity<Field> validateNotif(@RequestParam(value = "hashcode") String hashcode) throws Exception {

        try {
            Field f = Notification.validate(ownerRepository,repository, fieldrepository, pendingRepository, groundTypeRepository, plotRepository, hashcode);
            String idUser = String.valueOf(f.getOwner().getIdOwner());
            String registrationToken = null;

            //get the registration token

            Firestore db = FirestoreClient.getFirestore();
            System.out.println(db);
            Query query = db.collection("fcmtoken").whereEqualTo("idUser", idUser);
            ApiFuture<QuerySnapshot> future = query.get();
            
            // Block and wait for the future to resolve
            QuerySnapshot querySnapshot = future.get();
            // Block and wait for the future to resolve

            
            
            if (!querySnapshot.isEmpty()) {
                // There should ideally be only one document for a unique userId
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                registrationToken = document.getString("token");
            }

            //Send notification
            sendNotificationToDevice(registrationToken, "Field validation", "Your field has been validated by the adminstrator");

            return new ResponseEntity<>(f, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/refuse")
    public  ResponseEntity<Object> refuseNotif(@RequestParam(value = "hashcode") String hashcode) {
        try {
            int id = Notification.refuse(repository, pendingRepository, picturesdRepository, localisationRepository, hashcode);

            String idUser = String.valueOf(id);
            String registrationToken = null;

            //get the registration token

            Firestore db = FirestoreClient.getFirestore();
            System.out.println(db);
            Query query = db.collection("fcmtoken").whereEqualTo("idUser", idUser);
            ApiFuture<QuerySnapshot> future = query.get();
            
            // Block and wait for the future to resolve
            QuerySnapshot querySnapshot = future.get();
            // Block and wait for the future to resolve

            if (!querySnapshot.isEmpty()) {
                // There should ideally be only one document for a unique userId
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                registrationToken = document.getString("token");
            }
            sendNotificationToDevice(registrationToken, "Field validation", "Your field creation has been delined by the adminstrator");
            
            return new ResponseEntity<>("Refused", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendNotificationToDevice(String registrationToken, String title, String body) throws FirebaseMessagingException {
    // Initialize FirebaseApp if not done already
        if (!FirebaseApp.getApps().isEmpty()) {
            FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            
            // Construct a message with the target token and notification parameters
            Message message = Message.builder()
                    .setToken(registrationToken)
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();
            
            // Send the message and log the result
            String messageId = messaging.send(message);
            System.out.println("Successfully sent message: " + messageId);
        } else {
            System.err.println("Firebase App not initialized!");
        }
    }

}
