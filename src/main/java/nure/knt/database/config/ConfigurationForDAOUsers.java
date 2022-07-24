package nure.knt.database.config;

import nure.knt.database.dao.HandlerSqlDAO;
import nure.knt.database.dao.mysql.entity.HandlerUser;
import nure.knt.database.idao.terms.fieldenum.CustomerField;
import nure.knt.database.idao.terms.fieldenum.IUserField;
import nure.knt.database.idao.terms.fieldenum.UserField;
import nure.knt.database.idao.tools.IConnectorGetter;
import nure.knt.entity.important.Customer;
import nure.knt.entity.important.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Configuration
@PropertySource("classpath:property/users/UserProperty.properties")
public class ConfigurationForDAOUsers {

    @Bean("Get_Object_By_Field_For_User")
    public Map<IUserField, Function<User,Object>> userFieldToObject(){
        Map<IUserField, Function<User,Object>> map = new HashMap<>();
        HandlerDAOForUsersConfig.setUserFieldToObject(map);
        return map;
    }

    @Bean("Get_Object_By_Field_For_Customer")
    public Map<IUserField, Function<Customer,Object>> customerFieldToObject(){
        Map<IUserField, Function<Customer,Object>> map = new HashMap<>();
        map.put(CustomerField.CUSTOMER_ID,User::getId);
        map.put(CustomerField.MALE,User::getId);
        HandlerDAOForUsersConfig.setUserFieldToObject(map);
        return map;
    }

    @Bean("Get_Name_By_Field_For_User")
    public  Map<IUserField,String> getNameUserFieldInDataBase(@Value("${dao.users.order.by.enums.properties}") String fileName,
                                                       @Value("${dao.terms.users.what.add}") String propertyStart){
        return HandlerSqlDAO.<IUserField>setNameScriptForEntityByValueUnmodifiable(fileName,propertyStart, UserField.values());
    }

    @Bean("Get_Name_By_Field_For_Customer")
    public  Map<IUserField,String> getNameCustomerFieldInDataBase(@Value("${dao.for.customer.field.name}") String fileName,
                                                          @Value("${dao.terms.customer.what.add}") String propertyStart,
                                                          @Qualifier("Get_Name_By_Field_For_User") Map<IUserField,String> userFieldName) {
        Map<IUserField,String> map = HandlerSqlDAO.<IUserField>setNameScriptForEntityByValue(fileName,propertyStart, CustomerField.values());
        map.putAll(userFieldName);
        return Collections.unmodifiableMap(map);
    }

}

class HandlerDAOForUsersConfig{
    public static <U extends User> void setUserFieldToObject(Map<IUserField, Function<U,Object>> map){
        map.put(UserField.ID,User::getId);

        map.put(UserField.NUMBER,User::getNumber);
        map.put(UserField.EMAIL,User::getEmail);
        map.put(UserField.USERNAME,User::getUsername);

        map.put(UserField.COUNTRY,User::getCountry);
        map.put(UserField.NAME,User::getName);
        map.put(UserField.ACTIVE,User::isActive);

        map.put(UserField.DATE_REGISTRATION,User::getDateRegistration);
        map.put(UserField.ROLE,User::getRole);
        map.put(UserField.TYPE_STATE,User::getTypeState);

    }
}
