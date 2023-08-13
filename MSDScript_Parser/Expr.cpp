/**
* \file Expr.cpp
* \brief contains method implementations of subclasses
*
* \author Reshma Raghavan
* \date 01-17-2023
*/

#include "Expr.h"
#include <stdexcept>
#include <sstream>


void Expr::pretty_print (std::ostream& ot){
    pretty_print_at(ot, prec_none);
}

/* Implementations of "Num" class */
/**
* \brief Constructor
* \param value an integer value
*
* \return nothing (it is a constructor so it just creates an object of type Num)
* */
Num::Num(int value){
    val = value;
}

/**
* \brief Overriding "equals" method in Expr (base) class
* Basically, we compare two Num objects and determine if they are equal to one another by comparing their member variables (int val)
* \param e Pointer to an expression
* \return true or false (it is a boolean)
*/
bool Num::equals(Expr *e) {
    Num* s = dynamic_cast<Num*>(e);
    if (s == nullptr){
        return false;
    }
    else {
        return this->val == s->val;
    }
}

/**
* \brief Num returns a number (an int)
* Basically, interp() interprets the value of the object. In this case, a Num object contains an integer; this is returned
* \param none
* \return int
*/
int Num::interp(){
    int value = val;
    return value;
}

/**
* \brief Num returns false since it IS NOT a variable and DOES NOT HAVE a variable
* The general rule of thumb is this function returns true if the Expression is a variable or has a variable
* \param none
* \return true or false (it is a boolean)
*/
bool Num::has_variable(){
    return false;
}

/**
* \brief Num will never contain a string, so it just returns the original value
* \param str a String value
* \param e a pointer to an Expression
* \return a new Num Expression (containing the same integer val)
*/
Expr* Num::subst (std::string str, Expr* e){
    return new Num(val);
}

void Num::print (std::ostream& ot){
    ot << std::to_string(val);
}

void Num::pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses){
    ot << std::to_string(val);
}

/* Implementations of "Add" class */

/**
* \brief Constructor
* \param left a pointer to an Expression
* \param right a pointer to an Expression
*
* \return nothing (it is a constructor so it just creates an object of type Add)
* */
Add::Add(Expr* left, Expr* right){
    lhs = left;
    rhs = right;
}

/**
* \brief Overriding "equals" method in Expr (base) class
* Basically, we compare two Add objects and determine if they are equal to one another by comparing their member variables (both Expr* lhs and Expr* rhs)
* \param e Pointer to an expression
* \return true or false (it is a boolean)
*/
bool Add::equals(Expr *e) {
    Add *s = dynamic_cast<Add*>(e);
    if (s == nullptr){
        return false;
    }
    else{
        return ((this->lhs->equals(s->lhs)) && (this ->rhs->equals(s->rhs)));
    }
}

/**
* \brief Add returns a number (an int)
* Basically, interp() interprets the value of the object. In this case, an Add object contains two expressions that should be added to one another; the sum is returned
* \param none
* \return the sum of two expressions
*/
int Add::interp(){
    int left  = lhs->interp();
    int right  = rhs->interp();
    return left+right;
}

/**
* \brief Add returns true if it HAS a variable
* The general rule of thumb is this function returns true if the Expression is a variable or has a variable
* In Add, we check if the lhs or the rhs HAVE a variable
* \param none
* \return true or false (it is a boolean)
*/
bool Add::has_variable(){
    return (lhs->has_variable() || rhs-> has_variable());
}

/**
* \brief If the first param (str) is in either lhs or rhs, we replace it with e and return
* \param str a String value
* \param e a pointer to an Expression
* \return a new Add Expression
*/
Expr* Add::subst (std::string str, Expr* e){
    return new Add(lhs->subst(str, e) , rhs->subst(str, e));
}

void Add::print (std::ostream& ot){
    ot << "(" << lhs->to_string() << "+" << rhs->to_string() << ")";
}

void Add::pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses){
    if(precedence >= prec_add){
        ot << "(";
        lhs->pretty_print_at(ot, prec_add);
        ot << " + ";
        rhs->pretty_print_at(ot, prec_none);
        ot << ")";
    }
    else{
        lhs->pretty_print_at(ot, prec_add);
        ot << " + ";
        rhs->pretty_print_at(ot, prec_none);
    }
}

/* Implementations of "Mult" class */

/**
* \brief Constructor
* \param left a pointer to an Expression
* \param right a pointer to an Expression
*
* \return nothing (it is a constructor so it just creates an object of type Add)
* */
Mult::Mult(Expr* left, Expr* right){
    lhs = left;
    rhs = right;
}

/**
* \brief Overriding "equals" method in Expr (base) class
* Basically, we compare two Mult objects and determine if they are equal to one another by comparing their member variables (both Expr* lhs and Expr* rhs)
* \param e Pointer to an expression
* \return true or false (it is a boolean)
*/
bool Mult::equals(Expr *e) {
    Mult *s = dynamic_cast<Mult*>(e);
    if (s == nullptr){
        return false;
    }
    else{
        return ((lhs->equals(s->lhs)) && (rhs->equals(s->rhs)));
    }
}

/**
* \brief Mult returns a number (an int)
* Basically, interp() interprets the value of the object. In this case, a Mult object contains two expressions that should be multiplied by one another; the product is returned
* \param none
* \return the product of two subexpression classes
*/
int Mult::interp(){
    int left  = lhs->interp();
    int right  = rhs->interp();
    return left*right;
}

/**
* \brief Mult returns true if it HAS a variable
* The general rule of thumb is this function returns true if the Expression is a variable or has a variable
* In Mult, we check if the lhs or the rhs HAVE a variable
* \param none
* \return true or false (it is a boolean)
*/
bool Mult::has_variable(){
    return (lhs->has_variable() || rhs-> has_variable());
}

/**
* \brief If the first param (str) is in either lhs or rhs, we replace it with e and return
* \param str a String value
* \param e a pointer to an Expression
* \return a new Mult Expression
*/
Expr* Mult::subst (std::string str, Expr* e){
    return new Mult(lhs->subst(str, e) , rhs->subst(str, e));
}

void Mult::print (std::ostream& ot){
    ot << "(" << lhs->to_string() << "*" << rhs->to_string() << ")";
}

void Mult::pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses){
    if(precedence == prec_mult){
        ot << "(";
        lhs->pretty_print_at(ot, prec_mult);
        ot << " * ";
        rhs->pretty_print_at(ot, prec_add);
        ot << ")";
    }
    else{
        lhs->pretty_print_at(ot, prec_mult);
        ot << " * ";
        rhs->pretty_print_at(ot, prec_add);
    }
}

/* Implementations of "Var" class */

/**
* \brief Constructor
* \param value a String value
*
* \return nothing (it is a constructor so it just creates an object of type Var)
* */
Var::Var(std::string value){
    val = value;
}

/**
* \brief Overriding "equals" method in Expr (base) class
* Basically, we compare two Var objects and determine if they are equal to one another by comparing their member variables (String val)
* \param e Pointer to an expression
* \return true or false (it is a boolean)
*/
bool Var::equals(Expr *e){
    Var *s = dynamic_cast<Var*>(e);
    if (s == nullptr){
        return false;
    }
    else {
        return this->val == s->val;
    }
}

/**
* \brief A Var has no int equivalent
* \param none
* \return throws a runtime error
*/
int Var::interp(){
    throw std::runtime_error("Cannot interpret a variable");
}

/**
* \brief Var returns true as it IS a variable
* The general rule of thumb is this function returns true if the Expression is a variable or has a variable
* \param none
* \return true or false (it is a boolean)
*/
bool Var::has_variable(){
    return true;
}

/**
* \brief If the first param (str) is in the original Var, we replace it with e and return
* \param str a String value
* \param e a pointer to an Expression
* \return a new Var Expression
*/
Expr* Var::subst (std::string str, Expr* e){
    if (val == str){ // if the str is in the given Var (this)
        return e; // replace it and return accordingly
    }
    else return new Var(val); // else return the given expression
}

void Var::print (std::ostream& ot){
    ot << val;
}

void Var::pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses){
    ot << val;
}


/* Implementations of "Let" class */

/**
* \brief Constructor
* \param value an int value
*
* \return nothing (it is a constructor so it just creates an object of type _let)
* */
Let::Let(std::string left, Expr* right, Expr* bodyExpr){
    left = lhs;
    right = rhs;
    bodyExpr = body;
}

bool Let::equals(Expr *e){
    Let *s = dynamic_cast<Let*>(e);
    if (s == nullptr){
        return false;
    }
    else{
        return this->lhs == s->lhs && this->rhs == s->rhs && this->body == s->body;
    }
}

int Let::interp(){
    // interp RHS and save it
    int n = this->rhs->interp();
    // wrap a new object around n, and then substitute
    return (this->body->subst(lhs, new Num(n)))->interp();
}

bool Let::has_variable() {
    return rhs->has_variable() || body->has_variable();
}

Expr* Let::subst (std::string str, Expr* e) {
    // subst -> compare with variable
    // if it's true, don't touch the body
    // if it's not true, modify/substitute

    if (str == lhs) {
        return new Let(lhs, rhs->subst(str, rhs), body);
    } else {
        return new Let(lhs, rhs->subst(str, rhs), body->subst(str, e));
    }
}

void Let::print (std::ostream& ot){
    ot << "(" << "_let " << lhs << "=" << rhs->to_string() << " _in " << body->to_string() <<  ")";
}
void Let::pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses){

}