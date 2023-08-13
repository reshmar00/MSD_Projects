/**
* \file cmdline.cpp
* \brief contains single method to run executable
*
*
* \brief Runs executable with 0 or more arguments passed in
* \param argc first argument, number of arguments
* \param argv second argument, pointer to array of argument values
* \return nothing (it is a void function)
*
* \author Reshma Raghavan
* \date 01-11-2023
*/
#define CATCH_CONFIG_RUNNER

#include "cmdline.hpp"
#include <string>
#include <iostream>
#include <regex>

#include "catch.h"


void use_arguments(int argc, char **argv){
    bool testStatus = true;
    std::cout << "No. of arguments: " << argc << std::endl;
    if(argc == 1){
        std::cout << "Only one argument, the program name: "
                  << argv[0] << std::endl;
    }
    else if(argc > 1){
        std::cout << "Argument value(s): ";
        std::regex str("[a-zA-Z]+"); // any string w/ or w/o punctuation
        std::regex help("(--help)"); // "help" string
        std::regex test("(--test)"); // "test" string
        for(int i = 1; i < argc; i++){
            // Mention what the argument is
            std::cout << i << ") " << argv[i] << " ";
            // Conditions for this (next) argument
            std::string next = argv[i];
            if(std::regex_match(next, help)){ // if the argument is "--help"
                std::cout << std::endl;
                std::cout << "Type '--test' as an argument or use no arguments"
                          << std::endl;
                exit(0);
            }
            else if(std::regex_match(next, test)){ // if the argument is "--test"
               if (!testStatus){
                   std::cerr << "Tests already passed. Exiting." << std::endl;
                   testStatus = true;
                   exit(1);
               }
               testStatus = false;
               if (Catch::Session().run(1, argv) != 0){
                   exit(1);
               }
            }
            else{
                std::cout << std::endl;
                std::cerr << "Bad flag." << std::endl;
                exit(1);
            }
        }
    }
    // If no arguments trigger an error or exit, then use_arguments should return
    return;
}
