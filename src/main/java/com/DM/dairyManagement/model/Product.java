package com.DM.dairyManagement.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Long unit;
    private Long companyPrice;
    private Long retailerPrice;
    private Long wholesalerPrice;
    private Long customerPrice;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit1;


    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(Long id, String name, Long unit, Long companyPrice, Long retailerPrice, Long wholesalerPrice,
			Long customerPrice, String description, Unit unit1, Company company) {
		super();
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.companyPrice = companyPrice;
		this.retailerPrice = retailerPrice;
		this.wholesalerPrice = wholesalerPrice;
		this.customerPrice = customerPrice;
		this.description = description;
		this.unit1 = unit1;
		this.company = company;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}

	public Long getCompanyPrice() {
		return companyPrice;
	}

	public void setCompanyPrice(Long companyPrice) {
		this.companyPrice = companyPrice;
	}

	public Long getRetailerPrice() {
		return retailerPrice;
	}

	public void setRetailerPrice(Long retailerPrice) {
		this.retailerPrice = retailerPrice;
	}

	public Long getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Long wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public Long getCustomerPrice() {
		return customerPrice;
	}

	public void setCustomerPrice(Long customerPrice) {
		this.customerPrice = customerPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Unit getUnit1() {
		return unit1;
	}

	public void setUnit1(Unit unit1) {
		this.unit1 = unit1;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", unit=" + unit + ", companyPrice=" + companyPrice
				+ ", retailerPrice=" + retailerPrice + ", wholesalerPrice=" + wholesalerPrice + ", customerPrice="
				+ customerPrice + ", description=" + description + ", unit1=" + unit1 + ", company=" + company + "]";
	}

    
}
