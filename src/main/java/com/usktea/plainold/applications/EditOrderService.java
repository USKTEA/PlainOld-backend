package com.usktea.plainold.applications;

import com.usktea.plainold.applications.order.GetOrderService;
import com.usktea.plainold.applications.user.GetUserService;
import com.usktea.plainold.dtos.EditOrderRequest;
import com.usktea.plainold.models.order.Order;
import com.usktea.plainold.models.order.OrderNumber;
import com.usktea.plainold.models.user.Username;
import com.usktea.plainold.models.user.Users;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EditOrderService {
    private final GetUserService getUserService;
    private final GetOrderService getOrderService;

    public EditOrderService(GetUserService getUserService,
                            GetOrderService getOrderService) {
        this.getUserService = getUserService;
        this.getOrderService = getOrderService;
    }

    public OrderNumber edit(Username username, EditOrderRequest editOrderRequest) {
        Users user = getUserService.find(username);
        Order order = getOrderService.find(editOrderRequest.orderNumber());

        order.edit(user.username(), editOrderRequest);

        return order.orderNumber();
    }
}
