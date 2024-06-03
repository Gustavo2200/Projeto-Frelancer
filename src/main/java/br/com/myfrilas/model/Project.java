package br.com.myfrilas.model;

import java.util.Date;

import br.com.myfrilas.enums.StatusProject;

public class Project {
    
    private String title;
    private String description;
    private double price;
    private Client client; 
    private Freelancer freelancer;
    private Date startDate;
    private Date endDate;
    private StatusProject status;

    public Project() {}
    
    public Project(String title, String description, double price, Client client, Freelancer freelancer, Date startDate,
            Date endDate, StatusProject status) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.client = client;
        this.freelancer = freelancer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Freelancer getFreelancer() {
        return freelancer;
    }
    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public StatusProject getStatus() {
        return status;
    }
    public void setStatus(StatusProject status) {
        this.status = status;
    }
}
