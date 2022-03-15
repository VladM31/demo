package com.example.demo.controller;

import com.example.demo.Test;
import com.example.demo.dao.idao.IDAOCustomer;
import com.example.demo.dao.idao.IDAOSecurity;
import com.example.demo.dao.idao.IDAOTravelAgency;
import com.example.demo.entity.Customer;
import com.example.demo.entity.TravelAgency;
import com.example.demo.entity.User;
import com.example.demo.forms.ChooseSignUpForm;
import com.example.demo.forms.CustomerForm;
import com.example.demo.forms.TravelAgencyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
public class SecurityControler {

    private IDAOCustomer<Customer> daoCustomer;

    private IDAOTravelAgency<TravelAgency> daoTravelAgency;

    private IDAOSecurity daoSecurity;

    public SecurityControler() {
        try(ClassPathXmlApplicationContext context =
                    new ClassPathXmlApplicationContext("DAOContext.xml")) {

            daoCustomer = context.getBean("DAOCustomer",IDAOCustomer.class);
            daoTravelAgency = context.getBean("DAOTravelAgency",IDAOTravelAgency.class);
            daoSecurity = context.getBean("DAOSecurity",IDAOSecurity.class);
        } finally {
        }
    }

    @RequestMapping(value = { "/", "/mainWindow","/hello" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String showMainWindow(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {

            model.addAttribute("home", true);
            this.setMenuModel(user,model);
        }
        System.out.println(user);
        return "mainWindowPage";
    }

    @RequestMapping(value = { "/login"}, method = { RequestMethod.GET })
    public String signInGet(Model model) {

        return "login";
    }

    @RequestMapping(value = { "/logout"}, method = { RequestMethod.GET })
    public String signOutGet(@AuthenticationPrincipal User user,Model model) {
        this.setMenuModel(user,model);
        return "logout";
    }

    @RequestMapping(value= {"/sign_up"},method = { RequestMethod.GET })
    public String signUpGet(Model model)
    {
        model.addAttribute("setSignUp",new ChooseSignUpForm(null,false,true));

        return "choose_sign_upPage";
    }

    @RequestMapping(value= {"/sign_up"},method = { RequestMethod.POST })
    public String signUpPOST(Model model,ChooseSignUpForm form)
    {
        if (form.isEmpty()) {
            model.addAttribute("setSignUp", new ChooseSignUpForm(null, true, false));
            return "choose_sign_upPage";
        }

        if (form.isCustomer()) {
            model.addAttribute("customer",new CustomerForm());
            return "sign_up_customerPage";
        }else{
            model.addAttribute("travel",new TravelAgencyForm());
            return "sign_up_travel_agencyPage";
        }
    }

    @RequestMapping(value= {"/sign_up_error_customer"},method = { RequestMethod.POST })
    public String signUpCustomerPOST(Model model, CustomerForm form) {
        if (!form.hasGender()) {
            form.turnOnError();
            model.addAttribute("customer", form);
            return "sign_up_customerPage";
        }

        Customer temp = form.getCustomer();

        if(!this.daoSecurity.canAddThisUser(temp))
        {
            form.turnOnError();
            model.addAttribute("customer", form);
            return "sign_up_customerPage";
        }

        this.daoCustomer.save(temp);
        return "redirect:/login";
    }

    @RequestMapping(value= {"/sign_up_error_travel"},method = { RequestMethod.POST })
    public String signUpTravelAgencyPOST(Model model, TravelAgencyForm form) {
        if (form.isChooseEmpty()) {
            model.addAttribute("travel",form.getErrorForm());
            return "sign_up_travel_agencyPage";
        }

        TravelAgency temp = form.getTravelAgency();

        if(!this.daoSecurity.canAddThisUser(temp)) {
            model.addAttribute("customer", form.getErrorForm());
            return "sign_up_customerPage";
        }

        this.daoTravelAgency.save(temp);
        return "redirect:/login";
    }

    private void setMenuModel(User user,Model model) {
        model.addAttribute("sign_in", true);
        if (user instanceof Customer) {
            model.addAttribute("name", ((Customer) user).getFirstName());
        } else if (user instanceof TravelAgency) {
            model.addAttribute("name", ((TravelAgency) user).getNameTravelAgency());
        }
    }

}
