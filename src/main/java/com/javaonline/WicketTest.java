package com.javaonline;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.attributes.*;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import java.util.regex.Pattern;
import com.javaonline.dao.DAO;
import java.util.Date;
import java.nio.file.*;

public class WicketTest extends WebPage {

	private static final long serialVersionUID = 1L;
	RegBean regBean= new RegBean();
    
    PatternValidator validare_Nume = new PatternValidator(Pattern.compile("[A-Za-z]+"));  
    PatternValidator validare_Prenume = new PatternValidator(Pattern.compile("[A-Za-z]+([ -]{1}[A-Za-z]+)*"));   
    PatternValidator validare_Numar_Telefon = new PatternValidator(Pattern.compile("[0-9]{1,10}"));
    PatternValidator validare_Email = new PatternValidator(Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"));

	@SuppressWarnings({ "rawtypes", "serial" })
	public WicketTest() {
		
		final PropertyModel<String> strMdl = new PropertyModel<String>(regBean,"CV_NAME");
	    strMdl.setObject("Nici o selectie"); 
		
	    //declaratii variabile componente
        Label label1 = new Label("Nume", "Nume*");
        Label label2 = new Label("Prenume", "Prenume*");
        Label label3 = new Label("Telefon", "Telefon*");
        Label label4 = new Label("Email", "Adresa E-mail");

        final TextField<String> nume = new TextField<String>("NameField",new PropertyModel<String>(regBean,"NUME_APPLICANT"));
        final TextField<String> prenume = new TextField<String>("SurnameField",new PropertyModel<String>(regBean,"PRENUME_APPLICANT"));
        final TextField<String> telefon = new TextField<String>("PhoneNumber",new PropertyModel<String>(regBean,"TELEFON_APPLICANT"));
        final TextField<String> email = new EmailTextField("EmailField",new PropertyModel<String>(regBean,"EMAIL_APPLICANT"));
        final FileUploadField CV = new FileUploadField("CVUpload"); 
        final Label FileLabel = new Label("CVUploadLabel",strMdl);
        final Button Submit = new Button("Submit");
        
        //validare in timp real a input-ului la schimbarea unei casute text
        ValidareDinamicaInput(nume,"[A-Za-z]+");
        ValidareDinamicaInput(prenume, "[A-Za-z]+([ -]{1}[A-Za-z]+)*");
        ValidareDinamicaInput(telefon,"[0-9]{1,10}");
        ValidareDinamicaInput(email,"[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        
        //campul nu poate fi gol	
        nume.setRequired(true);
        prenume.setRequired(true);
        telefon.setRequired(true);
        CV.setRequired(true);
        
        //adaugare PatternValidator pentru validare input la trimiterea formularului
        nume.add(validare_Nume);
        prenume.add(validare_Prenume);
        telefon.add(validare_Numar_Telefon);
        email.add(validare_Email);
            
        FileLabel.setOutputMarkupId(true);
        
        //metoda ce seteaza label-ul fisierului cu numele fisierului selectat 
        CV.add(new AjaxEventBehavior("change"){

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
            {
                super.updateAjaxAttributes(attributes);

                String value = "return {'value': Wicket.$(attrs.c).value}";
                attributes.getDynamicExtraParameters().add(value);
            }

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                Request request = RequestCycle.get().getRequest();

                String numeFisier = request.getRequestParameters().getParameterValue("value").toString();
                numeFisier = Paths.get(numeFisier).getFileName().toString();
                
              	if(numeFisier.endsWith(".doc") || numeFisier.endsWith(".docx") || numeFisier.endsWith(".pdf")) 
              			strMdl.setObject(numeFisier);
              		else strMdl.setObject("Fisier cu format nepermis");

            	target.add(FileLabel);

            }

        });
        
        //validare date la trimiterea formularului
        Form form = new Form("WicketTestForm"){
        	
        	@Override
			public void onSubmit() {  		
				final FileUpload fisierCV = CV.getFileUpload();
				
				if(fisierCV != null){
					try {
						final String numeFisier = fisierCV.getClientFileName();
						
						if(numeFisier.endsWith(".doc") || numeFisier.endsWith(".docx") || numeFisier.endsWith(".pdf")) { 
		                	byte[] bytesFisier = fisierCV.getBytes();
		                	regBean.setFisierCV(bytesFisier);
		                	}
						
						regBean.setID(String.valueOf((int) (Math.random() * 900000) + 100000));//generare ID
						regBean.setCV_DATE(new Date());//setare data curenta pentru scrierea in tabel
						
						CorectareNume(regBean.getName());
						CorectarePrenume(regBean.getSurname());
	
						System.out.println("Scriere cu succes");
						DAO RegistrationDao = new DAO();
					    RegistrationDao.add(regBean);
					
					} 
					
					catch (Exception e) {
						e.printStackTrace(); 
					}
					
	        	}

			}
        	
        	/* La apasarea butonului de submit, campul de tip <input type=file> se goleste automat,
        	 * dar label-ul asociat lui ramane cu numele ultimului fisier selectat, 
        	 * deci este necesara reinitializare
        	 */
        	
        	//reinitializare text pentru label fisier(FileLabel) cand form-ul nu este valid
        	@Override
			public void onValidate() {  
        		if(!nume.isValid() || !prenume.isValid() || !telefon.isValid())
        			strMdl.setObject("Nici o selectie"); 		
        	}
        	
        	//reinitializare text pentru label fisier(FileLabel) cand form-ul este valid
        	@Override
			public void onValidateModelObjects() {  
        		if(CV.isValid())
        			strMdl.setObject("Nici o selectie"); 		
        	}
        	
			};
        
        
        form.add(label1);
        form.add(label2);
        form.add(label3);
        form.add(label4);
        form.add(nume); 
        form.add(prenume);
        form.add(telefon);
        form.add(email);
        form.add(CV);
        form.add(FileLabel);
        form.add(Submit);
        
        add(form);
    }
	
	//schimbarea culorii campului
    private void ValidareCamp(TextField<String> field, String pattern) {
    	
    	if(field.getModelObject().isEmpty()) 
    			field.add(new AttributeModifier("style","border-color: #cccccc")); //margine gri
    		else if(field.getModelObject().matches(pattern)) 
    				field.add(new AttributeModifier("style","border-color: #00a500; box-shadow: 0px 1px 4px 0px rgba(00, 165, 00, 0.1)"));	//margine verde
    			else 
    				field.add(new AttributeModifier("style","border-color: red; box-shadow: 0px 1px 4px 0px rgba(255, 0, 0, 0.1)")); //margine rosie
    	  	
    	}
    
    /*
    metoda care seteaza evenimentul Ajax prin care se schimba 
    culorea campului text in functie de validitatea input-ului
    */
    @SuppressWarnings("serial")
	private void ValidareDinamicaInput(final TextField<String> field, final String pattern){

    	field.add(new AjaxEventBehavior("change") {

    	        @Override
    	        protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
    	        {
    	            super.updateAjaxAttributes(attributes);

                    String value = "return {'value': Wicket.$(attrs.c).value}";
    	            attributes.getDynamicExtraParameters().add(value);
    	        }

                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    Request request = RequestCycle.get().getRequest();     
                    field.setModelObject(request.getRequestParameters().getParameterValue("value").toString());
    	                
   	                ValidareCamp(field, pattern);

   	                target.add(field);
   	            }

   	        });

    	}
    
	//corectare forma input dupa ce trece de validarea initiala
    private void CorectareNume(String value) {
    	
    	if(!value.matches("[A-Z]{1}[a-z]+")) {
    		
    		if(Character.isLowerCase(value.charAt(0)))
    			value = value.substring(0,1).toUpperCase() + value.substring(1); // prima litera toUpperCase
    		
    		for(int i = 1; i<value.length(); i++)
    			if(Character.isUpperCase(value.charAt(i)))
    				value = value.substring(0,i) + value.substring(i, i + 1).toLowerCase() + value.substring(i + 1);
    				
    	}
    	
    	regBean.setName(value);
    	
    }
    
    
    private void CorectarePrenume(String value) {
    	
    	StringBuilder sb = new StringBuilder(value);
    	
    	if(!value.matches("[A-Z]{1}[a-z]+([ -]{1}[A-Z]{1}[a-z]+)*")) {
    		
    		if(Character.isLowerCase(value.charAt(0)))
    			value = value.substring(0,1).toUpperCase() + value.substring(1);
    		
    		for(int i = 1; i<value.length(); i++) {
    			if(value.charAt(i - 1) == ' ' || value.charAt(i - 1) == '-') {
    				if(Character.isLowerCase(value.charAt(i))) // dupa delimitator(spatiu sau "-") urmeaza litera mare
    					value = value.substring(0, i) + value.substring(i, i + 1).toUpperCase() + value.substring(i + 1);
    				else if(value.charAt(i) == ' ' || value.charAt(i) == '-') { // elimina spatii sau caractere "-" consecutive
    	    			sb.deleteCharAt(i);
    					value = sb.toString();
    					}
    			}
    				
    			else if(Character.isUpperCase(value.charAt(i)))
    					value = value.substring(0,i) + value.substring(i, i + 1).toLowerCase() + value.substring(i + 1);
    		}
    		
    	}
    	
    	regBean.setSurname(value);
    	
    }
    
	
}