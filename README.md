# Cyberpunk 2030

-------
# 1. Game arhitecture
>> ### Main structure
>> The project follows the state machine principle where each state comprises multiple scenes, and each scene contains several components such as maps, characters, and buttons. Modifying the current active scene or state can be easily achieved through its associated component, without affecting the underlying structure.
>
>> ### In game interations 
>> Dynamic components that can alter the game's guidelines will interact via the Mediator Design Pattern, providing elegant and well-structured code practices.
> 
>> ### Database
>> The Scene class includes methods that interact with the database. These database actions are responsible for saving information related to the scenes.
> 
>> ### Other things
>> The game has an organizing timing system that allows any component or scene to have multiple timers for various purposes. The assets-related data is loaded through an organized tmx and tsx manner, enabling easy additions and modifications.
# 2. Story  

>> ### Introduction
>> In a dystopian world ruled by the Illuminati, Blaze takes on the role of a lone fighter with extraordinary abilities. The fighter's mission is to stop the Illuminati from launching a mass destruction rocket that would devastate society.
>
>> ###  Dangers
>> To achieve this, Blaze must battle through various enemies, including gangsters, cyber gunners, drones, and robots, all while navigating traps and dangerous terrain. The fighter must also save innocent people caught in the crossfire, making tough decisions along the way.
>
>> ###  Challenges
>> To obtain the key codes needed to stop the rocket launch, the fighter must defeat the Illuminati general, a former American football player, and the boss of the Illuminati, a man with a powerful machine. Each of these battles presents a unique challenge, with the boss battle being particularly difficult.
>
>> ### Saving the world
>> Once Blaze has collected all the key codes, he must race to the rocket's launch site, battling soldiers who try to stop him. With his fighting skills and determination, Blaze deactivates the rocket just in time, saving society from certain destruction.
>
>> ### Happy end
>> As the fighter rides off into the sunset, he knows that his mission was worth the risks. Blaze have proven that even in a world ruled by the Illuminati, there is still hope for a better future.

# 3. Gameplay

# 4. Game Assets

# 5. User Interactions
