//
//  fallingApple.cpp
//  testSFML
//
//  Created by Michael Johnson on 9/21/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#include "fallingApple.hpp"
#include "basket.hpp"

//make the apple fall from the tree
void FallingApple::makeTheAppleFall (std::vector<sf::Sprite>& fallingApples){
    fallingApple.move(0, yvelocity);
}


//check that the image for the sprite works and apply it to our object
void FallingApple::fallingAppleLoaded () {
    if (!fallingAppleTexture.loadFromFile("../apple-new.png")) {
        std::cerr << "error while loading texture" << std::endl;
    }
    else {
        fallingApple.setTexture(fallingAppleTexture);
    }
}


//assemble all pieces of the falling apple when the whenever the spawn clock reaches 150
void FallingApple::buildFallingApple () {
    if (spawnclock > 150) {
        fallingAppleLoaded();
        spawnFallingApple(fallingApplePosition);
        fallingApple.setPosition(fallingApplePosition);
        fallingApples.push_back(fallingApple);
        //reset the spawnclock to 0 so the spawning doesn't happen continuously
        spawnclock = 0;
    }
    //increment the spawnclock so it keeps pace
    else {
        spawnclock++;
    }
}


//draw the assembled falling apple on the screen with it's variables
void FallingApple::drawFallingApple (sf::RenderWindow& window) {
    buildFallingApple();
    for (int i = 0; i < fallingApples.size(); i++) {
        fallingApples[i].move(0, yvelocity);
        window.draw(fallingApples[i]);
        destroyFallingApple();
    }
}

void FallingApple::destroyFallingApple() {
    for (int i = 0; i < fallingApples.size(); i++) {
        if (fallingApples[i].getPosition().y > 960) {
            fallingApples.erase(fallingApples.begin() + i);
        }
    }
}

void FallingApple::destroyFallingAppleOnCollision() {
    for (int i = 0; i < fallingApples.size(); i++) {
            fallingApples.erase(fallingApples.begin() + i);
    }
}


// assign the spawn coordinates for the apple on the left tree
void FallingApple::spawnAppleLeft (sf::Vector2f& spawnPositionLeft) {
    std::srand(time(NULL));
    spawnPositionLeft.x = rand()% 400 + 150;
    spawnPositionLeft.y = rand()% 400 + 150;
    
}


// assign the spawn coordinates for the apple on the right tree
void FallingApple::spawnAppleRight (sf::Vector2f& spawnPositionRight) {
    std::srand(time(NULL));
    spawnPositionRight.x = rand()% 1000 + 750;
    spawnPositionRight.y = rand()% 750 + 75;
}


// assign spawn coordinates for the apple to one of the trees
void FallingApple::spawnFallingApple (sf::Vector2f& spawnposition) {
    //get a ranom number and determine if it is even or odd
    if (rand() % 2 == 0) {
        //if the random number is even, the position is on the left tree
        spawnAppleLeft(spawnposition);
    }
    else {
        //if the random number is odd, the position is on the right tree
        spawnAppleRight(spawnposition);
    }
}

sf::FloatRect FallingApple::fallingAppleGlobal () {
    for (int i = 0; i < fallingApples.size(); i++)
    return fallingApples[i].getGlobalBounds();
}

bool FallingApple::fallingAppleBasketCollision(Basket& basket)
{
    if(fallingAppleGlobal().intersects(basket.getBasketGlobalBounds())){
        return true;
    }
    
    return false;
}
