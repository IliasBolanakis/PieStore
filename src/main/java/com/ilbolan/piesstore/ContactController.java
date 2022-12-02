package com.ilbolan.piesstore;
import com.ilbolan.piesstore.forms.ContactForm;
import com.ilbolan.piesstore.email.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = {"/contact","/contact-success"})
public class ContactController extends HttpServlet {

    /**
     * Dispatches client to contact.jsp
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        getServletContext()
                .getRequestDispatcher("/WEB-INF/templates/contact.jsp")
                .forward(request, response);
    }

    /**
     * Perform form validation, sets attribute and dispatches back to contact.jsp
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        ContactForm contactFormData = new ContactForm(
                request.getParameter("fullName"),
                request.getParameter("email"),
                request.getParameter("tel"),
                request.getParameter("message")
        );

        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ContactForm>> errors = validator.validate(contactFormData);

            if (errors.isEmpty()) { // no errors

                // send emails
                Email.sendEmailToClientContactForm(contactFormData);
                Email.sendEmailToAdminContactForm(contactFormData);

                request.setAttribute("success", true);

            } else { // errors
                StringBuilder errorMessage = new StringBuilder("" +
                        "<p>Η φόρμα περιέχει τα εξής λάθη:</p>" +
                        "<ul>");

                for (var error : errors)
                    errorMessage.append("<li>").append(error.getMessage()).append("</li>");

                errorMessage.append("</ul>");

                request.setAttribute("errors", errorMessage);
                request.setAttribute("formData", contactFormData);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // response
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/contact.jsp")
                    .forward(request, response);
        }
    }
}