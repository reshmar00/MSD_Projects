//
//  world.hpp
//  testappletreeGame
//
//  Created by Reshma Raghavan on 9/21/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#pragma once

#include <iostream>
#include <math.h>
#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>

#include "world.hpp"
#include "basket.hpp"
#include "squirrel.hpp"

class World
{
    
public:
    // Creates a window which displays the game
    World()
    : window(sf::VideoMode(1240,960), "Apple Tree Game")
    {
        
    }
    // Runs the game in the window
    void loop();
    
private:
    
    // Starts the game back again
    void reset();
    
    // Member variable to be referenced
    sf::RenderWindow window;

};
