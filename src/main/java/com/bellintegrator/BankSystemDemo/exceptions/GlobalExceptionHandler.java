//package com.bellintegrator.BankSystemDemo.exceptions;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(InvalidBalanceException.class)
//    public String handleInvalidBalanceException(InvalidBalanceException ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(AccountNotFoundException.class)
//    public String handleAccountNotFoundException(AccountNotFoundException ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";  // Название шаблона, который будет использоваться для отображения ошибки
//    }
//}
