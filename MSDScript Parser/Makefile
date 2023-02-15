#compile and link "main.cpp" with "cmdline.cpp" to create an executable named msdscript

SRCS = main.cpp cmdline.cpp Expr.cpp TestExpr.cpp
OBJS = main.o cmdline.o Expr.o TestExpr.o

CXXFLAGS = --std=c++14

msdscript: $(OBJS)
	$(CXX) $(CXXFLAGS) $(OBJS) -o msdscript

.PHONY: build

build: msdscript

main.o: main.cpp

cmdline.o: cmdline.cpp cmdline.hpp catch.h

Expr.o: Expr.cpp Expr.h

TestExpr.o: TestExpr.cpp

.PHONY: clean

clean:
	rm *.o msdscript

.PHONY: test

test: msdscript
	./msdscript --test


.PHONY: doc

doc: 
	cd documentation && doxygen

.PHONY: showdoc

showdoc:
	cd documentation/html && open index.html
