//
//  thrownApple.hpp
//  testSFML
//
//  Created by Michael Johnson on 9/21/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#pragma once
#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>
#include <iostream>
#include <vector>
#include <stdlib.h>
#include <time.h>
#include <random>


class ThrownApple {
private:
    
    sf::Texture apple;
    sf::Sprite thrownApple;
    float xvelocity = -2.0;
    std::vector<sf::Sprite> thrownApples;
    sf::Vector2f thrownApplePosition;
    int shotClock = 0;
    


public:
    
    
    void thrownAppleLoaded ();
    
    sf::FloatRect getBounds();

    
    void throwTheApple ();
    
    
    
    void setThrowPosition (sf::Vector2f& basketPosition);

    
    void drawThrownApple (sf::RenderWindow& window, int& shotClock);
    
    void destroyThrownApple ();
    
};

