//
//  squirrel.hpp
//  testappletreeGame
//
//  Created by Reshma Raghavan on 9/22/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#pragma once

#include <iostream>
#include <math.h>
#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>

#include "thrownApple1.hpp"
#include "world.hpp"
#include "basket.hpp"
#include "squirrel.hpp"

class Squirrel{
    
private:
    sf::Texture squirrel;
    sf::Sprite squirrelSprite;
    sf::Vector2f squirrelPosition;
    bool onScreen = false;
    
    
public:
    ~Squirrel();
    
    Squirrel();
    
    Squirrel(float posOfSquirrelX, float posOfSquirrelY);
    
    void drawSquirrel(sf::RenderWindow& window);
    
    sf::FloatRect getSquirrelGlobalBounds();
    
    void moveSquirrel();
    
    
    bool squirrelThrownAppleCollision (ThrownApple& appleThrown);
    
    void resetSquirrelPosition ();

};
