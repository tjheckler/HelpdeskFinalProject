package controllers;

import com.google.common.io.Files;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class SiteAdminController extends ApplicationController
{
    private JPAApi jpaApi;
    private FormFactory formFactory;
    SiteRolesValues siteRole = new SiteRolesValues();

    @Inject
    public SiteAdminController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional(readOnly = true)
    public Result getSiteAdmins()
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {
            DynamicForm form = formFactory.form().bindFromRequest();
            String sql = "SELECT s FROM SiteAdmin s " +
                    "WHERE SiteAdminName LIKE :searchCriteria " +
                    "ORDER BY SiteAdminName";

            // add a join and search region, category, phone number role and email address
            String searchCriteria = form.get("searchCriteria");
            if (searchCriteria == null)
            {
                searchCriteria = "";
            }
            String queryParameter = searchCriteria + "%";

            List<SiteAdmin> siteAdmins = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("searchCriteria", queryParameter).getResultList();
            String locationSql = "SELECT l FROM Location l ";
            List<Location> location = jpaApi.em().createQuery(locationSql, Location.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            return ok(views.html.SiteAdmin.siteadminList.render(siteRoles, siteAdmins, searchCriteria, location));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("Login As Administrator"));
        }

    }

    @Transactional(readOnly = true)
    public Result getSiteAdmin(Integer siteAdminId)
    {
        if (isLoggedIn() && siteAdminId == getLoggedInSiteAdminId()
                || isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {
            String sql = "SELECT s FROM SiteAdmin s " +
                    "WHERE siteAdminId = :siteAdminId";
            SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("siteAdminId", siteAdminId).getSingleResult();

            String locationSql = "SELECT l FROM Location l ";
            List<Location> location = jpaApi.em().createQuery(locationSql, Location.class).getResultList();

            String regionSql = "SELECT r FROM Region r ";
            List<Region> region = jpaApi.em().createQuery(regionSql, Region.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            return ok(views.html.SiteAdmin.siteadmin.render(siteRoles, siteAdmin, location,
                    region, "* Indicates Required Field", ""));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("You Are Not Logged In"));
        }
    }

    @Transactional
    public Result postSiteAdmin(Integer siteAdminId)
    {


        if (isLoggedIn() && siteAdminId == getLoggedInSiteAdminId()
                || isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {

            String sql = "SELECT s FROM SiteAdmin s " +
                    "WHERE siteAdminId = :siteAdminId";
//add a join
            SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class)
                    .setParameter("siteAdminId", siteAdminId).getSingleResult();
            DynamicForm form = formFactory.form().bindFromRequest();

            String siteAdminName = form.get("siteAdminName");
            int locationId = Integer.parseInt(form.get("locationId"));
            String phoneNumber = form.get("phoneNumber");
            String emailAddress = form.get("emailAddress");
            int role = Integer.parseInt(form.get("siteRole"));
            String username = form.get("username");
            String password = form.get("password");


            if (siteAdminName != null && phoneNumber != null && emailAddress != null &&
                    role < 5 && username != null && password != null
                    && locationId > 0 && siteAdminId > 0)
            {
                try
                {
                    byte salt[] = Password.getNewSalt();
                    byte hashedPassword[] = Password.hashPassword(password.toCharArray(), salt);

                    siteAdmin.setSiteAdminName(siteAdminName);
                    siteAdmin.setPhoneNumber(phoneNumber);
                    siteAdmin.setSiteRole(role);
                    siteAdmin.setLocationId(locationId);
                    siteAdmin.setEmailAddress(emailAddress);
                    siteAdmin.setPasswordSalt(salt);
                    siteAdmin.setPassword(hashedPassword);
                    siteAdmin.setUsername(username);
                    jpaApi.em().persist(siteAdmin);
                } catch (Exception e)
                {
                    e.getCause();
                }


            } else
            {
                return redirect(routes.SiteAdminController.getSiteAdmins());
            }
        } else
        {
            return redirect(routes.AdministrationController.getLogin("Login As Administrator"));
        }
        return redirect(routes.SiteAdminController.getSiteAdmin(siteAdminId));
    }

    @Transactional(readOnly = true)
    public Result getSiteAdminEdit(Integer siteAdminId)
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {


            String sql = "SELECT s FROM SiteAdmin s " +
                    "WHERE siteAdminId = :siteAdminId";
            SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("siteAdminId", siteAdminId).getSingleResult();

            String locationSql = "SELECT l FROM Location l ";
            List<Location> location = jpaApi.em().createQuery(locationSql, Location.class).getResultList();

            String regionSql = "SELECT r FROM Region r ";
            List<Region> region = jpaApi.em().createQuery(regionSql, Region.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            return ok(views.html.SiteAdmin.siteadminedit.render(siteRoles, siteAdmin, location,
                    region, "* Indicates Required Field"));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("You Are Not Logged In"));
        }
    }

    @Transactional
    public Result postSiteAdminEdit(Integer siteAdminId)
    {

        String sql = "SELECT s FROM SiteAdmin s " +
                "WHERE siteAdminId = :siteAdminId";

        SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class)
                .setParameter("siteAdminId", siteAdminId).getSingleResult();
        DynamicForm form = formFactory.form().bindFromRequest();

        String siteAdminName = form.get("siteAdminName");
        int locationId = Integer.parseInt(form.get("locationId"));
        String phoneNumber = form.get("phoneNumber");
        String emailAddress = form.get("emailAddress");
        int role = Integer.parseInt(form.get("role"));
        String username = form.get("username");


        if (isLoggedIn() && getLoggedInSiteAdminRole().equals("1"))
        {
            String regionSql = "SELECT r FROM Region r ";

            List<Region> regions = jpaApi.em().createQuery
                    (regionSql, Region.class).getResultList();

            String locationSql = "SELECT l FROM Location l ";

            List<Location> locations = jpaApi.em().createQuery
                    (locationSql, Location.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            //Check for existing siteAdmin
            sql = "SELECT sa FROM SiteAdmin sa " +
                    "WHERE username = :username " +
                    "OR emailAddress = :emailAddress";

            List<SiteAdmin> siteAdmins = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("username", username).
                    setParameter("emailAddress", emailAddress).getResultList();

            if (getLoggedInSiteAdminRole().equals("1"))
            {
                if (siteAdminName != null && phoneNumber != null && (phoneNumber.length() >= 9)
                        && emailAddress != null && emailAddress.contains("@") &&
                        username != null && locationId > 0 && role > 0)
                {

                    siteAdmin.setSiteAdminName(siteAdminName);
                    siteAdmin.setPhoneNumber(phoneNumber);
                    siteAdmin.setSiteRole(role);
                    siteAdmin.setLocationId(locationId);
                    siteAdmin.setEmailAddress(emailAddress);
                    siteAdmin.setUsername(username);

                    jpaApi.em().persist(siteAdmin);

                } else
                {
                    return redirect(routes.SiteAdminController.getSiteAdmin(siteAdminId));
                }

            } else
            {
                return ok(views.html.SiteAdmin.siteadminedit.render(siteRoles, siteAdmin, locations, regions,
                        "User Already Exists Try Changing Both Email And Username Or Delete"));
            }
        } else
        {
            return redirect(routes.AdministrationController.getLogin("Login As Administrator"));
        }
        return redirect(routes.SiteAdminController.getSiteAdmin(siteAdminId));
    }

    @Transactional(readOnly = true)
    public Result getNewSiteAdmin(String message2)
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {
            DynamicForm form = formFactory.form().bindFromRequest();

            SiteAdminFormValues siteAdminFormValues = new SiteAdminFormValues();
            siteAdminFormValues.setAdminSiteAdminName(form.get("siteAdmin"));
            siteAdminFormValues.setAdminLocationId(form.get("locationId"));
            siteAdminFormValues.setAdminPhoneNumber(form.get("phoneNumber"));
            siteAdminFormValues.setAdminEmailAddress(form.get("emailAddress"));
            siteAdminFormValues.setAdminSiteRole(form.get("role"));
            siteAdminFormValues.setAdminUsername(form.get("username"));
            siteAdminFormValues.setAdminPassword(form.get("password"));


            String regionSql = "SELECT r FROM Region r ";

            List<Region> regions = jpaApi.em().createQuery
                    (regionSql, Region.class).getResultList();

            String locationSql = "SELECT l FROM Location l ";

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            List<Location> locations = jpaApi.em().createQuery
                    (locationSql, Location.class).getResultList();

            //Check if siteAdmin already exists by username or emailAddress

            String sql = "SELECT sa FROM SiteAdmin sa " +
                    "WHERE username = :username " +
                    "OR emailAddress = :emailAddress";

            List<SiteAdmin> siteAdmins = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("username", siteAdminFormValues.getAdminUsername()).
                    setParameter("emailAddress", siteAdminFormValues.getAdminEmailAddress()).getResultList();

            return ok(views.html.SiteAdmin.newsiteadmin.render(siteRoles, regions,
                    locations, "* Indicates Required Field Double Check Spelling Errors Before Submit",
                    "", siteAdminFormValues, true));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("You Are Not Logged In"));
        }
    }

    @Transactional
    public Result postNewSiteAdmin()
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {
            DynamicForm form = formFactory.form().bindFromRequest();

            SiteAdminFormValues siteAdminFormValues = new SiteAdminFormValues();
            siteAdminFormValues.setAdminSiteAdminName(form.get("siteAdmin"));
            siteAdminFormValues.setAdminLocationId(form.get("locationId"));
            siteAdminFormValues.setAdminPhoneNumber(form.get("phoneNumber"));
            siteAdminFormValues.setAdminEmailAddress(form.get("emailAddress"));
            siteAdminFormValues.setAdminSiteRole(form.get("role"));
            siteAdminFormValues.setAdminUsername(form.get("username"));
            siteAdminFormValues.setAdminPassword(form.get("password"));


            String regionSql = "SELECT r FROM Region r ";

            List<Region> regions = jpaApi.em().createQuery
                    (regionSql, Region.class).getResultList();

            String locationSql = "SELECT l FROM Location l ";

            List<Location> locations = jpaApi.em().createQuery
                    (locationSql, Location.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();
            //Check for existing siteAdmin
            String sql = "SELECT sa FROM SiteAdmin sa " +
                    "WHERE username = :username " +
                    "OR emailAddress = :emailAddress";

            List<SiteAdmin> siteAdmins = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("username", siteAdminFormValues.getAdminUsername()).
                    setParameter("emailAddress", siteAdminFormValues.getAdminEmailAddress()).getResultList();


            if (siteAdmins.size() == 1)
            {
                return ok(views.html.SiteAdmin.newsiteadmin.render(siteRoles, regions,
                        locations, "",
                        "User Already Exists Try Another Email Or Username",
                        siteAdminFormValues, true));
            } else
            {

                if (siteAdminFormValues.isValid())
                {


                    SiteAdmin siteAdmin = new SiteAdmin();
                    try
                    {
                        String flag = "True";
                        byte salt[] = Password.getNewSalt();
                        byte hashedPassword[] = Password.hashPassword(siteAdminFormValues
                                .getAdminPassword().toCharArray(), salt);

                        siteAdmin.setSiteAdminName(siteAdminFormValues.getAdminSiteAdminName());
                        siteAdmin.setPhoneNumber(siteAdminFormValues.getAdminPhoneNumber());
                        siteAdmin.setSiteRole(new Integer(siteAdminFormValues.getAdminSiteRole()));
                        siteAdmin.setLocationId(new Integer(siteAdminFormValues.getAdminLocationId()));
                        siteAdmin.setEmailAddress(siteAdminFormValues.getAdminEmailAddress());
                        siteAdmin.setPasswordSalt(salt);
                        siteAdmin.setPassword(hashedPassword);
                        siteAdmin.setUsername(siteAdminFormValues.getAdminUsername());
                        siteAdmin.setFlag(flag);
                    } catch (Exception e)
                    {
                        e.getCause();
                    }
                    jpaApi.em().persist(siteAdmin);


                } else
                {
                    return ok(views.html.SiteAdmin.newsiteadmin.render(siteRoles, regions,
                            locations, "* Indicates Required Field",
                            "", siteAdminFormValues, false));
                }
            }
            return redirect(routes.SiteAdminController.getSiteAdmins());

        } else
        {
            return redirect(routes.AdministrationController.getLogin("You Are Not Logged In"));
        }

    }

    @Transactional
    public Result deleteSiteAdmin(int siteAdminId)
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {

            String regionSql = "SELECT r FROM Region r ";

            List<Region> regions = jpaApi.em().createQuery
                    (regionSql, Region.class).getResultList();

            String sql = "SELECT sa FROM SiteAdmin sa " +
                    "WHERE siteAdminId = :siteAdminId";
            SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class).
                    setParameter("siteAdminId", siteAdminId).getSingleResult();

            String locationSql = "SELECT l FROM Location l ";

            List<Location> locations = jpaApi.em().createQuery
                    (locationSql, Location.class).getResultList();

            String siteRoleSql = "SELECT sr FROM SiteRole sr";
            List<SiteRole> siteRoles = jpaApi.em().createQuery(siteRoleSql, SiteRole.class).getResultList();

            String ticketSql = "SELECT t FROM Ticket t " +
                    "WHERE siteAdminId = :siteAdminId ";
            List<Ticket> tickets = jpaApi.em().createQuery(ticketSql, Ticket.class).
                    setParameter("siteAdminId", siteAdminId).getResultList();


            if (tickets.size() == 1)
            {
                //do nothing

            } else if (tickets.size() == 0)
            {
                jpaApi.em().remove(siteAdmin);
                return redirect(routes.SiteAdminController.getSiteAdmins());
            }
            return ok(views.html.SiteAdmin.siteadmin.render(siteRoles, siteAdmin,
                    locations, regions, "",
                    "* Cannot Delete, User is Assigned to at least one Ticket *"));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("Login As Administrator"));
        }

    }

    @Transactional(readOnly = true)
    public Result getPicture(int siteAdminId)
    {
        String sql = "SELECT sa FROM SiteAdmin sa " +
                "WHERE siteAdminId = :siteAdminId";
        SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class).
                setParameter("siteAdminId", siteAdminId).getSingleResult();
        return ok(siteAdmin.getPicture()).as("image/jpg");
    }

    @Transactional
    public Result postPicture(int siteAdminId)
    {
        String sql = "SELECT sa FROM SiteAdmin sa " +
                "WHERE siteAdminId = :siteAdminId";
        SiteAdmin siteAdmin = jpaApi.em().createQuery(sql, SiteAdmin.class).
                setParameter("siteAdminId", siteAdminId).getSingleResult();
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> filePart = formData.getFile("file");
        File file = filePart.getFile();

        if (file != null)
        {
            try
            {
                siteAdmin.setPicture(Files.toByteArray(file));
                jpaApi.em().persist(siteAdmin);
            } catch (Exception e)
            {
                e.getCause();
            }
        }
        return redirect(routes.SiteAdminController.getSiteAdmin(siteAdminId));
    }


}
