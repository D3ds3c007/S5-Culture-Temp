package com.culture.API.Controllers;

import com.culture.API.Models.Field;
import com.culture.API.Models.Owner;
import com.culture.API.Models.MongodbEntity.FieldPictures;
import com.culture.API.Repository.FieldPicturesRepository;
import com.culture.API.Repository.FieldRepository;
import com.culture.API.Repository.OwnerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "*", methods= {RequestMethod.POST, RequestMethod.GET})
@RestController
@RequestMapping("/api")
public class FieldController {
    
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    FieldPicturesRepository picturesRepository;

    @GetMapping("/fields")
    public ResponseEntity<List<Field>>  getAllFields() {
        try{
             List<Field> f=Field.findAll(fieldRepository);
             return new ResponseEntity<>(f,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
             return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/owner/fields")
    public ResponseEntity<List<Field>>  getFieldsByOwner(@RequestParam int idUser) {
        try{
             Owner o = Owner.findOwnerById(idUser, ownerRepository);
             List<Field> f=Field.findFieldByOwner(fieldRepository, o);
             return new ResponseEntity<>(f,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/field")
    public ResponseEntity<Field> insertField(@RequestBody Field field) {
        try{
            Field fi = Field.saveField(field, fieldRepository);
            return new ResponseEntity<>(fi,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/picture")
    public ResponseEntity<FieldPictures> getPicture(@RequestParam(value = "hashcode") String hashcode) {
        try{
            FieldPictures pic = FieldPictures.findByHashcode(hashcode, picturesRepository);
            return new ResponseEntity<>(pic,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
