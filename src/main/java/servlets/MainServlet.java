package servlets;

import model.Account;
import model.BudgetRow;
import model.Record;
import model.dao.Service;
import model.dao.impl.ServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet(urlPatterns = {"", "/main","/addaccount","/delaccount","/addbudgetrow","/delbudgetrow","/addrecord","/delrecord"})
public class MainServlet extends HttpServlet {

    Service service;

    @Override
    public void init(){
        this.service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(getPage());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String command = req.getServletPath();

        if (command.contains("/addaccount")) {

            String accountName = req.getParameter("account_name");
            if (accountName == null || accountName.equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else{
                service.addAccount(null, accountName);
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        }
        else if (command.contains("/delaccount")){

            String strId = req.getParameter("id");
            if (strId == null || strId.equals("")){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else {

                try {
                    long id = Long.parseLong(strId);
                    service.deleteAccount(id);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }

        }else if (command.contains("/addbudgetrow")) {

            String budgetRowName = req.getParameter("budget_row_name");
            if (budgetRowName == null || budgetRowName.equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else {
                service.addBudgetRow(budgetRowName, 0);
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        }else if (command.contains("/delbudgetrow")){

                String strId = req.getParameter("id");
                if (strId == null || strId.equals("")){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                else {
                    try {
                        long id = Long.parseLong(strId);
                        service.deleteBudgetRow(id);
                        resp.setStatus(HttpServletResponse.SC_OK);

                    } catch (NumberFormatException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }

        }else if (command.contains("/addrecord")){

            try{
                Account sender = service.getAccountById(Long.parseLong(req.getParameter("record_account_sender")));
                BudgetRow budgetRow = service.getBudgetRowById(Long.parseLong(req.getParameter("record_budget_row")));
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("record_date"));
                int sum = Integer.parseInt(req.getParameter("record_sum"));

                service.addRecord(date, sender, null, budgetRow, sum);
                resp.setStatus(HttpServletResponse.SC_OK);
            }catch (ParseException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }

        else if (command.contains("/delrecord")){
            String strId = req.getParameter("id");
            if (strId == null || strId.equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }else {
                try {
                    long id = Long.parseLong(strId);
                    service.deleteRecord(id);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }

        }

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(getPage());

    }

    String getPage() throws IOException {

        String page = new String(this.getClass().getClassLoader().getResourceAsStream("main.html").readAllBytes());

        //page is empty, we should fill it by data
        String accountsArea = "";
        List<Account> accounts = service.getAccountByName("");

        for(Account acc: accounts){

            accountsArea = accountsArea+"<tr>"+
                   "<td>"+acc.getId()+"</td>"+
                   "<td>"+acc.getName()+"</td>" +
                   "<td>"+
                    "<form action=\"./delaccount?id="+acc.getId()+"\" method=\"POST\">"+
                    "   <input type=\"submit\" value = \"X delete\"/>"+
                    "</form>"+
                    "</td>"+
                    "</tr>";

        }
        page = page.replaceFirst("==Accounts==", accountsArea);


        //filling budget rows area
        String budgetRowsArea = "";
        List<BudgetRow> budgetRows = service.getBudgetRowByName("");

        for(BudgetRow budgetRow: budgetRows){

            budgetRowsArea = budgetRowsArea+"<tr>"+
                    "<td>"+budgetRow.getId()+"</td>"+
                    "<td>"+budgetRow.getName()+"</td>" +
                    "<td>"+
                    "<form action=\"./delbudgetrow?id="+budgetRow.getId()+"\" method=\"POST\">"+
                    "   <input type=\"submit\" value = \"X delete\"/>"+
                    "</form>"+
                    "</td>"+
                    "</tr>";

        }
        page = page.replaceFirst("==BudgetRows==", budgetRowsArea);


        //filling records area
        String recordBudgetRowsArea = "";
        for(BudgetRow budgetRow: budgetRows){
            recordBudgetRowsArea = recordBudgetRowsArea +"<option value="+budgetRow.getId()+">"+budgetRow.getName()+"</option>";
        }
        page = page.replaceFirst("==RecordListOfBudgetRows==", recordBudgetRowsArea);


        String recordAccountsArea = "";
        for(Account acc: accounts){

            recordAccountsArea = recordAccountsArea+"<option value="+acc.getId()+">"+acc.getName()+"</option>";

        }
        page = page.replaceFirst("==RecordListOfAccounts==", recordAccountsArea);



        String recordsArea = "";
        List<Record> records = service.getRecordsBySender(null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for(Record record: records){

            recordsArea = recordsArea+"<tr>"+
                    "<td>"+record.getId()+"</td>"+
                    "<td>"+dateFormat.format(record.getDate())+"</td>" +
                    "<td>"+record.getSender()+"</td>" +
                    "<td>"+record.getBudgetRow()+"</td>" +
                    "<td>"+record.getSum()+"</td>" +
                    "<td>"+
                    "<form action=\"./delrecord?id="+record.getId()+"\" method=\"POST\">"+
                    "   <input type=\"submit\" value = \"X delete\"/>"+
                    "</form>"+
                    "</td>"+
                    "</tr>";

        }
        page = page.replaceFirst("==Records==", recordsArea);

        return page;

    }

}
