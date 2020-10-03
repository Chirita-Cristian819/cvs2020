package com.javaonline;
 
import java.io.Serializable;
import java.util.Date;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Lob;
 
@Entity
@Table(name="candidat_application")
public class RegBean implements Serializable {
 
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Id
@Column(name="ID")
private String ID="";

public String getID() {
	return ID;
}
public void setID(String regId) {
	ID = regId;
}

@Column(name="NUME_APPLICANT")
private String NUME_APPLICANT;

public String getName() {
	return  NUME_APPLICANT;
}
public void setName(String name) {
	NUME_APPLICANT = name;
}

@Column(name="PRENUME_APPLICANT")
private String PRENUME_APPLICANT;

public String getSurname() {
	return  PRENUME_APPLICANT;
}
public void setSurname(String name) {
	PRENUME_APPLICANT = name;
}

@Column(name="TELEFON_APPLICANT")
private String TELEFON_APPLICANT;

public String getTelefon() {
	return  TELEFON_APPLICANT;
}
public void setTelefon(String tel) {
	TELEFON_APPLICANT = tel;
}

@Column(name="EMAIL_APPLICANT")
private String EMAIL_APPLICANT;

public String getEmail() {
	return  EMAIL_APPLICANT;
}
public void setEmail(String email) {
	EMAIL_APPLICANT = email;
}

@Column(name="CV_NAME")
private String CV_NAME="";

public String getDenumireCV() {
	return  CV_NAME;
}
public void setDenumireCV(String numeCV) {
	CV_NAME = numeCV;
}

@Column( name="CV_FISIER")
@Lob
private byte[] CV_FISIER;

public byte[] getFisierCV() {
	return CV_FISIER;
}

public void setFisierCV(byte[] Fisier) {
	CV_FISIER = Fisier;
}

@Temporal(TemporalType.DATE)
@Column (name="CV_APPLICATION_DATE")
private Date CV_APPLICATION_DATE;

public Date CV_DATE() {
	return new Date(); 
}
public void setCV_DATE(Date data) {
	CV_APPLICATION_DATE = data;
}

 
}