//
//  thrownApple.cpp
//  testSFML
//
//  Created by Michael Johnson on 9/21/22.
//
//  Group members: Michael Johnson and Reshma Raghavan

#include "thrownApple1.hpp"
#include "basket.hpp"

Basket basket;

void ThrownApple::thrownAppleLoaded () {
    if (!apple.loadFromFile("../apple-new.png")) {
        std::cerr << "error while loading texture" << std::endl;
    }
    else {
        thrownApple.setTexture(apple);
    }
}


void ThrownApple::throwTheApple () {
//    thrownAppleLoaded();
//    thrownApple.setPosition(thrownApplePosition);
//    thrownApples.push_back(thrownApple);
}



void ThrownApple::setThrowPosition (sf::Vector2f& basketPosition) {
        thrownApplePosition = basketPosition;
}


void ThrownApple::drawThrownApple (sf::RenderWindow& window, int& shotClock) {
    if(sf::Keyboard::isKeyPressed(sf::Keyboard::Space) && shotClock > 10){
    shotClock = 0;
    thrownAppleLoaded();
    thrownApple.setPosition(thrownApplePosition);
    thrownApples.push_back(thrownApple);
//    throwTheApple();
    }
    for (int i = 0; i < thrownApples.size(); i++) {
        thrownApples[i].move(xvelocity, 0);
        window.draw(thrownApples[i]);
    }
}

void ThrownApple::destroyThrownApple () {
    for (int i = 0; i < thrownApples.size(); i++) {
            thrownApples.erase(thrownApples.begin() + i);
    }
}

sf::FloatRect ThrownApple::getBounds() {
    for (int i = 0; i < thrownApples.size(); i++)
    return thrownApples[i].getGlobalBounds();
}
