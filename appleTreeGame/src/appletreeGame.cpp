//
//  appletreeGame.cpp
//
//
//  Created by Reshma Raghavan on 9/20/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#include <iostream>
#include <math.h>
#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>

#include "fallingApple.hpp"
#include "thrownApple1.hpp"
#include "world.hpp"
#include "basket.hpp"
#include "squirrel.hpp"

using namespace std;

int main ()
{
    World world;
    world.loop();
    
    return 0;
}
