//
//  squirrel.cpp
//  testappletreeGame
//
//  Created by Reshma Raghavan on 9/22/22.
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

#include <iostream>

Squirrel::~Squirrel(){
}

Squirrel::Squirrel()
{

    this->squirrel.loadFromFile("../squirrel-new.png");
    
    this->squirrelSprite.setTexture(this->squirrel);
    this->squirrelSprite.setPosition(50.f, 625.f);
    
    // Update squirrelPosition to the correct position on screen
    this->squirrelPosition.x = 50.f;
    this->squirrelPosition.y = 625.f;
    
    if(!squirrel.loadFromFile("../squirrel-new.png")){
        std::cerr << "Error while loading texture" << std::endl;
    }
}

Squirrel::Squirrel(float posOfSquirrelX, float posOfSquirrelY)
{
    this->squirrel.loadFromFile("../squirrel-new.png");
    this->squirrelSprite.setTexture(this->squirrel);
    
    this->squirrelSprite.setPosition(posOfSquirrelX, posOfSquirrelY);
    
    // Update squirrelPosition to the correct position on screen
    this->squirrelPosition.x = posOfSquirrelX;
    this->squirrelPosition.y = posOfSquirrelY;
}

void Squirrel::drawSquirrel(sf::RenderWindow& window){
    window.draw(squirrelSprite);
}

sf::FloatRect Squirrel::getSquirrelGlobalBounds()
{
    return squirrelSprite.getGlobalBounds();
}

void Squirrel::moveSquirrel(){
        squirrelSprite.move(3, 0);
    }

bool Squirrel::squirrelThrownAppleCollision (ThrownApple& appleThrown) {
    if (getSquirrelGlobalBounds().intersects(appleThrown.getBounds())){
        return true;
    }
    else {
        return false;
    }
}

void Squirrel::resetSquirrelPosition () {
    squirrelSprite.setPosition(50.f, 625.f);
    squirrelSprite.move(0,0);
}
