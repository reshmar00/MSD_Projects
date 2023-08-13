#declaration of variables
CXX = c++
CFLAGS = -std=c++14
CXXSOURCE = $(wildcard *.cpp)
HEADERS = $(wildcard *.hpp)
OBJECTS = $(CXXSOURCE:.cpp=.o)
#rule that creates the program
prog: $(OBJECTS)
	$(CXX) $(CFLAGS) $(OBJECTS) -lreadline -o prog
#rule that generates objects files
%.o: %.cpp $(HEADERS)
	$(CXX)  $(CFLAGS) -c $< -o $@
	
#rule that cleans out put binary files and the executable program
.PHONY: clean
clean:
	rm -rf *.o prog
