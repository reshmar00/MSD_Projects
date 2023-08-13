/**
* \file TestExpr.cpp
* \brief contains test cases of Expression subclass methods
*
* \author Reshma Raghavan
* \date 01-23-2023
*/


#include "catch.h"
#include "Expr.h"
TEST_CASE( "equals" ) {
/**
* \brief Testing the "equals" method. Asserting true or false as the case may be.
* */
    /* Number */
    SECTION("Num"){
        CHECK( (new Num(1))->equals(new Num(1)) == true );
        CHECK( (new Num(1))->equals(new Num(-1)) == false );
        CHECK( (new Num(-1))->equals(new Num(-1)) == true );
        CHECK( (new Num(1))->equals(new Num(10)) == false );
        CHECK( (new Num(-1))->equals(new Num(-11)) == false );
        CHECK( (new Num(0))->equals(new Num(0)) == true );
        CHECK( (new Num(0))->equals(new Var("0")) == false );
        CHECK( (new Num(6))->equals(new Add(new Num(3),new Num(3))) == false );
        CHECK( (new Num(16))->equals(new Mult(new Num(2),new Num(8))) == false );
    }

    /* Variable */
    SECTION("Var"){
        CHECK( (new Var("x"))->equals(new Var("x")) == true );
        CHECK( (new Var("x"))->equals(new Var("X")) == false );
        CHECK( (new Var("x"))->equals(new Var("y")) == false );
        CHECK( (new Var("123"))->equals(new Var("123")) == true );
        CHECK( (new Var("a34c"))->equals(new Var("a34c")) == true );
        CHECK( (new Var("#$!0"))->equals(new Var("#$!0")) == true );
        CHECK( (new Var("eleven"))->equals(new Var("11")) == false );
        CHECK( (new Var("11"))->equals(new Num(11)) == false );
        CHECK( (new Var("25"))->equals(new Add(new Num(10),new Num(15))) == false );
        CHECK( (new Var("100"))->equals(new Mult(new Num(25),new Num(4))) == false );
    }

    /* Add */
    SECTION("Add"){
        CHECK( (new Add(new Num(7),new Num(3)))->equals(new Add(new Num(7),new Num(3))) == true );
        CHECK( (new Add(new Num(7),new Num(3)))->equals(new Add(new Num(8),new Num(2))) == false );
        CHECK( (new Add(new Num(7),new Num(3)))->equals(new Add(new Num(3),new Num(7))) == false );
        CHECK( (new Add(new Num(-5),new Num(5)))->equals(new Add(new Num(-5),new Num(5))) == true );
        CHECK( (new Add(new Num(-5),new Num(5)))->equals(new Add(new Num(5),new Num(-5))) == false );
        CHECK( (new Add(new Num(-5),new Num(-5)))->equals(new Add(new Num(-5),new Num(-5))) == true );
        CHECK( (new Add(new Num(12),new Num(5)))->equals(new Num(17)) == false );
        CHECK( (new Add(new Num(12),new Num(5)))->equals(new Var("17")) == false );
        CHECK( (new Add(new Num(3),new Num(3)))->equals(new Mult(new Num(3),new Num(2))) == false );
    }

    /* Mult */
    SECTION("Mult"){
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Mult(new Num(10),new Num(11))) == true );
        CHECK( (new Mult(new Num(-10),new Num(11)))->equals(new Mult(new Num(-10),new Num(11))) == true );
        CHECK( (new Mult(new Num(10),new Num(-11)))->equals(new Mult(new Num(10),new Num(-11))) == true );
        CHECK( (new Mult(new Num(-10),new Num(-11)))->equals(new Mult(new Num(-10),new Num(-11))) == true );
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Mult(new Num(11),new Num(10))) == false );
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Mult(new Mult(new Num(5),new Num(2)), new Num(11))) == false );
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Num(110)) == false );
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Var("110")) == false );
        CHECK( (new Mult(new Num(10),new Num(11)))->equals(new Add(new Num(55),new Num(55))) == false );
        CHECK( (new Mult(new Num(99),new Num(0)))->equals(new Mult(new Num(99),new Num(0))) == true );
        CHECK( (new Mult(new Num(99),new Num(0)))->equals(new Num(0)) == false );
    }
}

TEST_CASE( "interp" ) {
/**
* \brief Testing the "interp" method. Asserting true or false as the case may be.
* */
    /* Num */
    SECTION("Num") {
        CHECK( ( new Num(0) ) -> interp() == 0 );
        CHECK( ( new Num(-21) ) -> interp() == -21 );
        CHECK( ( new Num(-21) ) -> interp() != 21 );
        CHECK( ( new Num(32) ) -> interp() == 32 );
        CHECK( ( new Num(32) ) -> interp() != -32 );
    }
    /* Add */
    SECTION("Add") {
        /* Num + Num */
        CHECK( ( new Add( new Num(10), new Num(15) ) )->interp()==25);
        CHECK( ( new Add( new Num(0), new Num(15) ) )->interp()==15);
        CHECK( ( new Add( new Num(1), new Num(15) ) )->interp()==16);
        CHECK( ( new Add( new Num(-1), new Num(15) ) )->interp()==14);
        /* Add + Num */
        CHECK( ( new Add( new Add(new Num(10), new Num(15)), new Num(10) ) )->interp()==35);
        CHECK( ( new Add( new Num(-1), new Add(new Num(10), new Num(15)) ) )->interp()==24);
        /* Mult + Num */
        CHECK( ( new Add( new Num(-1), new Mult(new Num(10), new Num(2)) ) )->interp()==19);
        CHECK( ( new Add( new Mult(new Num(15), new Num(3)), new Num(10) ) )->interp()==55);
        /* Add + Add */
        CHECK( ( new Add( new Add(new Num(10), new Num(15)),
                        new Add(new Num(20),new Num(20))))
                       ->interp()==65);
        /* Mult + Mult */
        CHECK( ( new Add( new Mult(new Num(5), new Num(2)),
                          new Mult(new Num(20),new Num(5))))
                       ->interp()==110);
        /* Var + anything */
        CHECK_THROWS_WITH( (new Add (new Var("abc"), new Num(7)))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Add ( new Add(new Num(17), new Num(10)),
                                      new Var("XYZ")))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Add ( new Mult(new Num(17), new Num(10)),
                                      new Var("abc")))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Add (new Var("abc"), new Var("XYZ")))->interp(), "Cannot interpret a variable" );
    }
    /* Mult */
    SECTION("Mult") {
        /* Num x Num */
        CHECK( (new Mult(new Num(3), new Num(2))) ->interp()==6 );

        /* Add x Num */
        CHECK( (new Mult(new Add(new Num(7), new Num(7)), new Num(2))) ->interp()==28 );
        CHECK( (new Mult(new Add(new Num(10), new Num(90)), new Num(-2))) ->interp()==-200 );

        /* Mult x Num */
        CHECK( (new Mult(new Mult(new Num(10), new Num(2)), new Num(5))) ->interp()==100 );

        /* Add x Add */
        CHECK( (new Mult(new Add(new Num(10), new Num(5)), new Add(new Num(98), new Num(2)))) ->interp()==1500 );
        CHECK( (new Mult(new Add(new Num(25), new Num(15)), new Add(new Num(-45), new Num(5)))) ->interp()==-1600 );

        /* Mult x Mult */
        CHECK( (new Mult(new Mult(new Num(10), new Num(5)), new Mult(new Num(100), new Num(2)))) ->interp()==10000 );
        CHECK( (new Mult(new Mult(new Num(150), new Num(5)), new Mult(new Num(50), new Num(-10)))) ->interp()==-375000 );

        /* Mult x Add */
        CHECK( (new Mult(new Mult(new Num(-100), new Num(1)), new Add(new Num(100), new Num(300)))) ->interp()==-40000 );
        CHECK( (new Mult(new Add(new Num(-10), new Num(10)), new Mult(new Num(100), new Num(2)))) ->interp()==0 );

        /* Var x anything */
        CHECK_THROWS_WITH( (new Mult(new Var("3"), new Num(2))) ->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Mult(new Add(new Num(2), new Num(3)), new Var("ABCxyz"))) ->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Mult(new Var("snake_case_here"), new Mult(new Num(-2), new Num(50)))) ->interp(),"Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Mult(new Var("$#!@*#"), new Var("S2n1fk90!"))) ->interp(),"Cannot interpret a variable" );
    }
    /* Var */
    SECTION("Var") {
        CHECK_THROWS_WITH( (new Var("abc"))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Var("$#!@*#"))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Var("cAmElCaseISH"))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Var("snake_case_here"))->interp(), "Cannot interpret a variable" );
        CHECK_THROWS_WITH( (new Var("29462973000000"))->interp(), "Cannot interpret a variable" );
    }
}
TEST_CASE( "has_variable" ) {
/**
* \brief Testing the "has_variable" method. Asserting true or false as the case may be.
* */
    /* Num */
    SECTION("Num") {
        CHECK( ( new Num(0) ) -> has_variable() == false );
        CHECK( ( new Num(99) ) -> has_variable() == false );
        CHECK( ( new Num(-100) ) -> has_variable() == false );
        CHECK( ( new Num(999999) ) -> has_variable() == false );
        CHECK( ( new Num(-1100282910) ) -> has_variable() == false );
    }
    /* Add */
    SECTION("Add") {
        CHECK( ( new Add(new Num(0), new Num(10)) ) -> has_variable() == false );
        CHECK( ( new Add(new Var("x"), new Var("y")) ) -> has_variable() == true );
        CHECK( ( new Add(new Num(8), new Var("y")) ) -> has_variable() == true );
        CHECK( ( new Add(new Add(new Num(20), new Num(10)), new Num(50)) ) -> has_variable() == false );
        CHECK( ( new Add(new Mult(new Var("y"), new Num(70)), new Num(10)) ) -> has_variable() == true );
        CHECK( ( new Add(new Mult(new Num (10), new Num(20)), new Add(new Var("p"), new Num(50))) ) -> has_variable() == true );
        CHECK( ( new Add(new Mult(new Num(11), new Num(10)), new Mult(new Num(100), new Num(50))) ) -> has_variable() == false );
        CHECK( ( new Add(new Add(new Num(0), new Num(450)), new Add(new Num(-170), new Num(1))) ) -> has_variable() == false );
    }
    /* Mult */
    SECTION("Mult") {
        CHECK( ( new Mult(new Num(0), new Num(10)) ) -> has_variable() == false );
        CHECK( ( new Mult(new Var("x"), new Var("y")) ) -> has_variable() == true );
        CHECK( ( new Mult(new Num(8), new Var("y")) ) -> has_variable() == true );
        CHECK( ( new Mult(new Add(new Num(20), new Num(10)), new Num(50)) ) -> has_variable() == false );
        CHECK( ( new Mult(new Mult(new Var("y"), new Num(70)), new Num(10)) ) -> has_variable() == true );
        CHECK( ( new Mult(new Mult(new Num (10), new Num(20)), new Add(new Var("p"), new Num(50))) ) -> has_variable() == true );
        CHECK( ( new Mult(new Mult(new Num(11), new Num(10)), new Mult(new Num(100), new Num(50))) ) -> has_variable() == false );
        CHECK( ( new Mult(new Add(new Num(0), new Num(450)), new Add(new Num(-170), new Num(1))) ) -> has_variable() == false );
    }
    /* Var */
    SECTION("Var") {
        CHECK( (new Var("abc")) -> has_variable() == true );
        CHECK( (new Var("$#!@*#")) -> has_variable() == true );
        CHECK( (new Var("cAmElCaseISH")) -> has_variable() == true );
        CHECK( (new Var("snake_case_here")) -> has_variable() == true );
        CHECK( (new Var("29462973000000")) -> has_variable() == true );
    }
}
TEST_CASE( "subst" ) {
/**
* \brief Testing the "subst" method. Asserting true or false as the case may be.
* */
    /* Num */
    SECTION("Num") {
        // No changes
        CHECK( (new Num(0))->subst("x", new Var("y"))->equals(new Num(0) ));
        CHECK( (new Num(10))->subst("x", new Add(new Var("y"), new Num(0)))->equals(new Num(10) ));
        CHECK( (new Num(100000))->subst("abs", new Mult(new Var("abc"), new Num(9999)))->equals(new Num(100000) ));
        CHECK( (new Num(-999999))->subst("123", new Add(new Mult(new Num(55), new Num(65)),new Num(345)))->equals(new Num(-999999) ));
        CHECK( (new Num(10101099))->subst("x", new Mult(new Num(123), new Add(new Num(9999), new Num(-867))))->equals(new Num(10101099) ));
    }
    /* Add */
    SECTION("Add") {
        CHECK( (new Add(new Num(10), new Num(15)))->subst("x", new Var("y"))->equals(new Add(new Num(10), new Num(15))));
        CHECK( (new Add(new Var("x"), new Num(15)))->subst("x", new Var("y"))->equals(new Add(new Var("y"), new Num(15))));
        CHECK( (new Add(new Var("x"), new Var("y")))->subst("x", new Add(new Var("y"), new Num(45)))->equals(new Add(new Add(new Var("y"), new Num(45)), new Var("y"))));
        CHECK( (new Add(new Add(new Var("y"), new Num(99)), new Var("y")))->subst("y", new Add(new Var("z"), new Num(99)))->equals(new Add(new Add(new Add(new Var("z"), new Num(99)), new Num(99)), new Add(new Var("z"), new Num(99)))));
        CHECK( (new Add(new Mult(new Var("www"), new Num(99)), new Var("www")))->subst("www", new Var("zzz"))->equals(new Add(new Mult(new Var("zzz"), new Num(99)), new Var("zzz"))));
    }
    /* Mult */
    SECTION("Mult") {
        CHECK( (new Mult(new Num(10), new Num(15)))->subst("x", new Var("y"))->equals(new Mult(new Num(10), new Num(15))));
        CHECK( (new Mult(new Var("x"), new Num(15)))->subst("x", new Var("y"))->equals(new Mult(new Var("y"), new Num(15))));
        CHECK( (new Mult(new Var("x"), new Var("y")))->subst("x", new Mult(new Var("y"), new Num(45)))->equals(new Mult(new Mult(new Var("y"), new Num(45)), new Var("y"))));
        CHECK( (new Mult(new Add(new Var("y"), new Num(99)), new Var("y")))->subst("y", new Mult(new Var("z"), new Num(99)))->equals(new Mult(new Add(new Mult(new Var("z"), new Num(99)), new Num(99)), new Mult(new Var("z"), new Num(99)))));
        CHECK( (new Mult(new Mult(new Var("www"), new Num(99)), new Num(20)))->subst("www", new Var("zzz"))->equals(new Mult(new Mult(new Var("zzz"), new Num(99)), new Num(20))));
    }
    /* Var */
    SECTION("Var") {
        CHECK( (new Var("x"))->subst("x", new Add(new Var("y"),new Num(7)))->equals(new Add(new Var("y"),new Num(7))) );
        CHECK( (new Var("abc"))->subst("abc", new Var("xyz"))->equals(new Var("xyz")) );
        CHECK( (new Var("08429JFgbjh"))->subst("08429JFgbjh", new Add(new Var("Number"), new Num(1234)))->equals(new Add(new Var("Number"), new Num(1234))) );
        CHECK( (new Var("$#%$^%&&"))->subst("adding", new Var("someString"))->equals(new Var("$#%$^%&&" )));
        CHECK( (new Var("--~~``__"))->subst("--~~``__", new Var("lines"))->equals(new Var("lines" )));
    }
}


TEST_CASE("Mult pretty print") {
    Num *intTwo = new Num(2);
    Num *intThree = new Num(3);

    Mult *test_mult_prec = new Mult(intTwo, intThree);
    std::stringstream out("");
    test_mult_prec->pretty_print(out);
    CHECK(out.str() == "2 * 3");

    Mult *test_mult_prec_right = new Mult(intTwo, new Mult(intTwo, intThree));
    std::stringstream out2("");
    test_mult_prec_right->pretty_print(out2);
    CHECK(out2.str() == "2 * 2 * 3");

    Mult *test_mult_prec_left = new Mult( new Mult(intTwo, intThree), intTwo);
    std::stringstream outLeft("");
    test_mult_prec_left->pretty_print(outLeft);
    CHECK(outLeft.str() == "(2 * 3) * 2");
}

TEST_CASE("Add pretty print"){
    Num *intTwo = new Num(2);
    Num *intThree = new Num(3);

    Add *test_add_prec = new Add(intTwo, intThree);
    std::stringstream out("");
    test_add_prec->pretty_print(out);
    CHECK(out.str() == "2 + 3");

    Add *test_add_prec_right = new Add(intTwo, new Add(intTwo, intThree));
    std::stringstream out2("");
    test_add_prec_right->pretty_print(out2);
    CHECK(out2.str() == "2 + 2 + 3");

    Add *test_add_prec_left = new Add( new Add(intTwo, intThree), intTwo);
    std::stringstream outLeft("");
    test_add_prec_left->pretty_print(outLeft);
    CHECK(outLeft.str() == "(2 + 3) + 2");
}

TEST_CASE("Add/Mult pretty Print"){
    Num *intTwo = new Num(2);
    Num *intThree = new Num(3);

    Mult *test_add_prec_right = new Mult(intTwo, new Add(intTwo, intThree));
    std::stringstream out2("");
    test_add_prec_right->pretty_print(out2);
    CHECK(out2.str() == "2 * (2 + 3)");

    Add *test_add_prec_left = new Add( intTwo, new Mult(intTwo, intThree));
    std::stringstream outLeft("");
    test_add_prec_left->pretty_print(outLeft);
    CHECK(outLeft.str() == "2 + 2 * 3");
}