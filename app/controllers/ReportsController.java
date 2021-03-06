package controllers;

import models.*;

import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.print.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

public class ReportsController extends ApplicationController
{
    private JPAApi jpaApi;
    private FormFactory formFactory;
    SiteRolesValues siteRole = new SiteRolesValues();

    @Inject
    public ReportsController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional(readOnly = true)
    public Result getReports()
    {
        if (isLoggedIn() && getLoggedInSiteAdminRole().equals(siteRole.getAdmin()))
        {
            String categorySql = "SELECT  NEW TicketCategoryCount(c.categoryId, c.categoryName, COUNT(*)) " +
                    "FROM Ticket t " +
                    "JOIN Category c ON t.categoryId = c.categoryId " +
                    "GROUP BY c.categoryName " +
                    "ORDER BY c.categoryName";

            List<TicketCategoryCount> ticketCategoryCounts = jpaApi.em().
                    createQuery(categorySql, TicketCategoryCount.class).getResultList();

            String siteAdminSql = "SELECT  NEW TicketSiteAdminCount(t.siteAdminId, s.siteAdminName, COUNT(*)) " +
                    "FROM Ticket t " +
                    "JOIN SiteAdmin s ON t.siteAdminId = s.siteAdminId " +
                    "GROUP BY s.siteAdminName " +
                    "ORDER BY s.siteAdminName";

            List<TicketSiteAdminCount> ticketSiteAdminCounts = jpaApi.em().
                    createQuery(siteAdminSql, TicketSiteAdminCount.class).getResultList();

            String prioritySql = "SELECT  NEW TicketPriorityCount(p.priorityId, p.priorityName, COUNT(*)) " +
                    "FROM Ticket t " +
                    "JOIN Priority p ON t.priorityId = p.priorityId " +
                    "GROUP BY p.priorityName " +
                    "ORDER BY p.priorityName";

            List<TicketPriorityCount> ticketPriorityCounts = jpaApi.em().
                    createQuery(prioritySql, TicketPriorityCount.class).getResultList();

            String locationSql = "SELECT  NEW TicketLocationCount(l.locationId, l.locationName, COUNT(*)) " +
                    "FROM Ticket t " +
                    "JOIN Location l ON t.locationId = l.locationId " +
                    "GROUP BY l.locationName " +
                    "ORDER BY l.locationName";

            List<TicketLocationCount> ticketLocationCounts = jpaApi.em().
                    createQuery(locationSql, TicketLocationCount.class).getResultList();

            String regionSql = "SELECT  NEW TicketRegionCount(r.regionId, r.regionName, COUNT(*)) " +
                    "FROM Ticket t " +
                    "JOIN Location l ON l.locationId = t.locationId " +
                    "JOIN Region r ON l.regionId = r.regionId " +
                    "GROUP BY r.regionName " +
                    "ORDER BY r.regionName";

            List<TicketRegionCount> ticketRegionCounts = jpaApi.em().
                    createQuery(regionSql, TicketRegionCount.class).getResultList();

            String InventorySql = "SELECT  NEW InventoryLocationCount(l.locationId, l.locationName, COUNT(*)) " +
                    "FROM Inventory i " +
                    "JOIN Location l ON l.locationId = i.locationId " +
                    "GROUP BY l.locationName " +
                    "ORDER BY l.locationName";

            List<InventoryLocationCount> inventoryLocationCounts = jpaApi.em().
                    createQuery(InventorySql, InventoryLocationCount.class).getResultList();

            String replySql = "SELECT  NEW TicketReplyCount(r.ticketsId, t.computerName, COUNT(*)) " +
                    "FROM Reply r " +
                    "JOIN Ticket t ON t.ticketsId = r.ticketsId " +
                    "GROUP BY t.computerName " +
                    "ORDER BY t.computerName";

            List<TicketReplyCount> ticketReplyCounts = jpaApi.em().
                    createQuery(replySql, TicketReplyCount.class).getResultList();

            return ok(views.html.Report.reports.render(ticketCategoryCounts, ticketSiteAdminCounts,
                    ticketPriorityCounts, ticketLocationCounts, ticketRegionCounts,
                    inventoryLocationCounts, ticketReplyCounts));
        } else
        {
            return redirect(routes.AdministrationController.getLogin("Login As Administrator"));
        }
    }


}
