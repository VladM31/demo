package nure.knt.tempClasses.verify;

import nure.knt.database.idao.entity.IDAOCustomerSQL;
import nure.knt.entity.important.Customer;
import nure.knt.forms.signup.CustomerForm;
import nure.knt.tempClasses.verify.verify.inter.IVerifyCustomerForm;
import nure.knt.tempClasses.verify.verify.inter.IVerifySyntaxErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerifyTempCustomerForm implements IVerifyCustomerForm {

    private IDAOCustomerSQL<Customer> dataCustomer;
    private IVerifySyntaxErrors syntaxErrors;

    @Autowired
    public void setDataCustomer(IDAOCustomerSQL<Customer> dataCustomer) {
        this.dataCustomer = dataCustomer;
    }
    @Autowired
    public void setSyntaxErrors(IVerifySyntaxErrors syntaxErrors) {
        this.syntaxErrors = syntaxErrors;
    }

    @Override
    public String checkOut(CustomerForm cf) {

        if(!cf.hasGender()){
            return "Error:The gender dont choose";
        }
        if (syntaxErrors.hasProblemInUsername(cf.getUsername())){
            return "Error:The username is incorrect";
        }
        if (syntaxErrors.hasProblemInPassword(cf.getPassword())) {
            return "Error:The password is incorrect";
        }
        if (syntaxErrors.hasProblemInEmail(cf.getEmail())){
            return "Error:The email is incorrect";
        }
        if (dataCustomer.findByUsername(cf.getUsername()) != null) {
            return "Error:The username is busy";
        }
        if (dataCustomer.findByEmail(cf.getEmail()) != null) {
            return "Error:The email is busy";
        }

        return "Successful";
    }
}