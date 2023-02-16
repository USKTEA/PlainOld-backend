package com.usktea.plainold.controllers;

import com.usktea.plainold.applications.user.EditUserService;
import com.usktea.plainold.applications.user.GetUserService;
import com.usktea.plainold.dtos.EditUserRequest;
import com.usktea.plainold.dtos.EditUserRequestDto;
import com.usktea.plainold.dtos.EditUserResultDto;
import com.usktea.plainold.dtos.UserInformationDto;
import com.usktea.plainold.exceptions.EditUserFailed;
import com.usktea.plainold.exceptions.UserNotExists;
import com.usktea.plainold.models.user.Username;
import com.usktea.plainold.models.user.Users;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {
    private final GetUserService getUserService;
    private final EditUserService editUserService;

    public UserController(GetUserService getUserService, EditUserService editUserService) {
        this.getUserService = getUserService;
        this.editUserService = editUserService;
    }

    @GetMapping("me")
    public UserInformationDto user(
            @RequestAttribute Username username) {
        Users user = getUserService.find(username);

        return user.toDto();
    }

    @PatchMapping
    public EditUserResultDto edit(
            @RequestAttribute Username username,
            @Valid @RequestBody EditUserRequestDto editUserRequestDto
    ) {
        try {
            EditUserRequest editUserRequest = EditUserRequest.of(editUserRequestDto);

            Username edited = editUserService.edit(username, editUserRequest);

            return new EditUserResultDto(edited.value());
        } catch (Exception exception) {
            throw new EditUserFailed(exception.getMessage());
        }
    }

    @ExceptionHandler(UserNotExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotExists(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(EditUserFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editUserFailed(Exception exception) {
        return exception.getMessage();
    }
}
