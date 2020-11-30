package com.up.habit.expand.swagger.bean;

/**
 * TODO:
 * <p>
 *
 * @author 王剑洪 on 2019/10/25 13:50
 */
public class Info {
    private String description;
    private String version;
    private String title;

    private Contact contact;
    private License license;

    public Info(String title, String description, String version) {
        this.description = description;
        this.version = version;
        this.title = title;
    }

    public Info(String title, String description) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
