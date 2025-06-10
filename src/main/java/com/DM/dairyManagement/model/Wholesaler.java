package com.DM.dairyManagement.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class Wholesaler {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Long mobileNo;
    private String email;
    private String adharCardPath;
    private String passportPhotoPath;
    private String licenceCopyPath;
    private String vehicleNo;

    @Transient
    private MultipartFile adharCardPhoto;

    @Transient
    private MultipartFile passportPhoto;

    @Transient
    private MultipartFile licenceCopy;

    @ManyToOne
    @JoinColumn(name = "company_id")  
    private Company company;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getMobileNo() { return mobileNo; }
    public void setMobileNo(Long mobileNo) { this.mobileNo = mobileNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdharCardPath() { return adharCardPath; }
    public void setAdharCardPath(String adharCardPath) { this.adharCardPath = adharCardPath; }

    public String getPassportPhotoPath() { return passportPhotoPath; }
    public void setPassportPhotoPath(String passportPhotoPath) { this.passportPhotoPath = passportPhotoPath; }

    public String getLicenceCopyPath() { return licenceCopyPath; }
    public void setLicenceCopyPath(String licenceCopyPath) { this.licenceCopyPath = licenceCopyPath; }

    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }

    public MultipartFile getAdharCardPhoto() { return adharCardPhoto; }
    public void setAdharCardPhoto(MultipartFile adharCardPhoto) { this.adharCardPhoto = adharCardPhoto; }

    public MultipartFile getPassportPhoto() { return passportPhoto; }
    public void setPassportPhoto(MultipartFile passportPhoto) { this.passportPhoto = passportPhoto; }

    public MultipartFile getLicenceCopy() { return licenceCopy; }
    public void setLicenceCopy(MultipartFile licenceCopy) { this.licenceCopy = licenceCopy; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}
