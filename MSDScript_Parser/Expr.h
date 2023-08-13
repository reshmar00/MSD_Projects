/**
* \file Expr.h
* \brief contains declaration of the abstract class's methods; more information available in complementary .cpp file
*
* \author Reshma Raghavan
* \date 01-17-2023
*/

#ifndef EXPRESSION_CLASSES_EXPR_H
#define EXPRESSION_CLASSES_EXPR_H


#include <string>
#include <sstream>

class Expr {

/** \brief Expression class: it is an abstract class with virtual methods to be implemented in its derived classes
*
*/



public:
    typedef enum {
        prec_none = 0,
        prec_add = 1,
        prec_mult = 2
    } precedence_t;
    virtual bool equals(Expr* e) = 0;
    virtual int interp() = 0;
    virtual bool has_variable() = 0;
    virtual Expr* subst (std::string str, Expr* e) = 0;
    virtual void print (std::ostream& ot) = 0;
    std::string to_string(){
        std::stringstream st("");
        this->print(st);
        return st.str();
    };
    void pretty_print (std::ostream& ot);
    std::string to_string_pretty(){
        std::stringstream st("");
        this->pretty_print(st);
        return st.str();
    };
    virtual void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses) = 0;
};

class Num : public Expr {
public:
    int val;
    Num(int value);
    bool equals(Expr *e);
    int interp();
    bool has_variable();
    Expr* subst (std::string str, Expr* e);
    void print (std::ostream& ot);
    std::string to_string();
    std::string to_string_pretty();
    void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses);
};

class Add : public Expr {
public:
    Expr* lhs;
    Expr* rhs;
    Add(Expr* left, Expr* right);
    bool equals(Expr *e);
    int interp();
    bool has_variable();
    Expr* subst (std::string str, Expr* e);
    void print (std::ostream& ot);
    std::string to_string();
    std::string to_string_pretty();
    void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses);
};

class Mult: public Expr {
public:
    Expr* lhs;
    Expr* rhs;
    Mult(Expr* left, Expr* right);
    bool equals(Expr *e);
    int interp();
    bool has_variable();
    Expr* subst (std::string str, Expr* e);
    void print (std::ostream& ot);
    std::string to_string();
    std::string to_string_pretty();
    void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses);
};

class Var: public Expr {
public:
    std::string val;
    Var(std::string value);
    bool equals(Expr *e);
    int interp();
    bool has_variable();
    Expr* subst (std::string str, Expr* e);
    void print (std::ostream& ot);
    std::string to_string();
    std::string to_string_pretty();
    void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses);
};

class Let : public Expr {
public:
    std::string lhs;
    Expr* rhs;
    Expr* body;
    Let(std::string left, Expr* right, Expr* bodyExpr);
    bool equals(Expr *e);
    int interp();
    bool has_variable();
    Expr* subst (std::string str, Expr* e);
    void print (std::ostream& ot);
    std::string to_string();
    std::string to_string_pretty();
    void pretty_print_at(std::ostream& ot, precedence_t precedence, std::streampos streamPosition, bool letParentheses);
};

#endif //EXPRESSION_CLASSES_EXPR_H
