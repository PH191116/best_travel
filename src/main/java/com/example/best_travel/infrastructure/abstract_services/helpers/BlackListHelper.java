package com.example.best_travel.infrastructure.abstract_services.helpers;

import com.example.best_travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {
    public void isBlackListCustomer(String customerId){
        if (customerId.equals("VIKI771012HMCRG093")){
            throw new ForbiddenCustomerException();
        }
    }
}
