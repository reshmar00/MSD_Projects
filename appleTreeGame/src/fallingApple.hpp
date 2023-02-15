//
//  fallingApple.hpp
//  testSFML
//
//  Created by Michael Johnson on 9/21/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#pragma once

#include "thrownApple1.hpp"
#include "basket.hpp"

class FallingApple
{
private:
    
    sf::Texture fallingAppleTexture;
    sf::Sprite fallingApple;
    float yvelocity = 2;
    int spawnclock = 0;
    std::vector<sf::Sprite> fallingApples;
    sf::Vector2f fallingApplePosition;
//    std::random_device rando;
//    std::mt19937 mersenne(rando());
//    std::uniform_real_distribution<> randomtest (0,1);

    
public:
    
    //check that the texture and sprite loaded correctly
    void fallingAppleLoaded ();
    
    //make the apple fall from the tree
    void makeTheAppleFall (std::vector<sf::Sprite>& fallingApples);
    
    //assemble all pieces of the falling apple whenever the spawn clock reaches 150
    void buildFallingApple ();

    //draw the assembled falling apple on the screen with it's variables
    void drawFallingApple (sf::RenderWindow& window);
    
    // assign the spawn coordinates for the apple on the left tree
    void spawnAppleLeft (sf::Vector2f& spawnPositionLeft);
    
    // assign the spawn coordinates for the apple on the right tree
    void spawnAppleRight (sf::Vector2f& spawnPositionRight);
    
    // assign spawn coordinates for the apple to one of the trees
    void spawnFallingApple (sf::Vector2f& spawnposition);
    
    sf::FloatRect fallingAppleGlobal ();
    
    bool fallingAppleBasketCollision(Basket& basket);
    
    void destroyFallingApple ();
    
    void destroyFallingAppleOnCollision();

};
