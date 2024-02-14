package com.culture.API.Models.MongodbEntity;

import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.culture.API.Models.DTO.OwnerDTO;
import com.culture.API.Repository.FieldRepository;
import com.culture.API.Repository.GroundTypeRepository;
import com.culture.API.Repository.NotificationRepository;
import com.culture.API.Repository.OwnerRepository;
import com.culture.API.Repository.PendingFieldRepository;
import com.culture.API.Repository.PlotRepository;
import com.culture.API.Repository.FieldPicturesRepository;
import com.culture.API.Repository.FieldLocalisationRepository;

import jakarta.persistence.Basic;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.culture.API.Models.*;


@Document(collection="Notification")
public class Notification {
    
    private OwnerDTO owner;

    @Basic
    private String hashcode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Notification(OwnerDTO owner, String hashcode, Date date) {
        this.owner = owner;
        this.hashcode = hashcode;
        this.date = date;
    }
    public Notification() {
        
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }
    
    public static List<Notification> findAll(NotificationRepository repository) throws SQLException {
        List<Notification> listNotif = repository.findAll();
        return listNotif;
    }

    public static Notification save(Notification notif, NotificationRepository repository) throws SQLException {
        Notification n = repository.save(notif);
        return n;
    }

    public static Notification findById(String hashcode, NotificationRepository repository)
    {
        return repository.findByHashcode(hashcode);
    }

    public static Field validate(OwnerRepository ownerRepository, NotificationRepository repository, FieldRepository fieldRepository, PendingFieldRepository pendingRepository, GroundTypeRepository groundTypeRepository, PlotRepository plotRepository, String hashcode) throws SQLException{
        Notification notif = repository.findByHashcode(hashcode);
        PendingField pending = pendingRepository.findByHashcode(hashcode);
        Owner owner = Owner.findOwnerById(notif.getOwner().getIdOwner(), ownerRepository);
        Field f = new Field();
            f.setOwner(owner);
            f.setHashcode(pending.getHashcode());
            f.setLocation(pending.getLocation());
            f.setDescription(pending.getDescription());
            f.setArea(pending.getArea());

        

        try {
            Field savedField = Field.saveField(f, fieldRepository);
            int plotNumber = pending.getPlotNumber();
            double plotArea = pending.getArea() / plotNumber;
            System.out.println(plotNumber);

            GroundType g = GroundType.findGroundTypeById(pending.getGroundType(), groundTypeRepository);

            for (int i = 0; i < plotNumber; i++) {
                Plot plot = new Plot(savedField, plotArea, g);
                System.out.println(i);

                Plot.savePlot(plot, plotRepository);
            }

                repository.deleteByHashcode(notif.getHashcode());
                pendingRepository.deleteByHashcode(hashcode);
                
            return savedField;
        } catch (Exception e) {
            return null;
        }
        
    }

    public static int refuse(NotificationRepository repository, PendingFieldRepository pendingRepository, FieldPicturesRepository picturesRepository, FieldLocalisationRepository localisationRepository, String hashcode){
        try {
            Notification notif = repository.findByHashcode(hashcode);
            int idOwner = notif.getOwner().getIdOwner();
            repository.deleteByHashcode(hashcode);
            pendingRepository.deleteByHashcode(hashcode);
            picturesRepository.deleteByHashcode(hashcode);
            localisationRepository.deleteByHashcode(hashcode);
            return idOwner;

        } catch (Exception e) {
            return 0;
        }
    }
    
}
