package com.spring_boot_api_p2.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }

  public ResourceNotFoundException(String message){
        super(message);
  }
}
