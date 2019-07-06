package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SLF4JExample {
   public static void main(String[] args) {
      //Creating the Logger object
      Logger logger = LoggerFactory.getLogger("SampleLogger");

      //Logging the information
      logger.info("Welcome to Yiibai.com SLF4J Yiibai.");
      logger.debug("sss");
      logger.error("err");
     // System.out.println("hell");
   }
}