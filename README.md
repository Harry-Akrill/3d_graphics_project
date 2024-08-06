## Harry Akrill - 3D Graphics Project
This project renders a set of aliens in a snowy scene with various added animations.

## How to run
Using the Aliens.bat file, the project can be ran. The project can also be ran using the following three commands:
'del *.class'
'javac Aliens.java'
'java Aliens'

## References + Code implementation
The majority of work implemented in the classes has been influenced heavily by the lab class work provided by Dr Steve Maddock.
Classes Alien1,Alien2,Aliens_GLEventListener,Aliens,Backdrop,SecurityLight,SecurityPost are influenced by those lab classes, however a majority of them is my own work. Many of the shaders have been adapted and renamed from the lab classes, they are used as follows :
'fs_alien_2t.txt' and 'vs_sphere_01.txt' are used by Alien1 and Alien2.
'vs_standard.txt' is used by all of the walls, the scene wall uses 'fs_scenewall_2t.txt', and the other walls use 'fs_sidewall_2t.txt'. The floor uses 'fs_standard_1t.txt'.
The security light post uses 'fs_standard_1t.txt'.
'fs_light_01.txt' and 'vs_light_01.txt' are from the lab code, and 'fs_light_02.txt' and 'vs_light_02.txt' are the attempted implementations of the Security spotlight.

Both rock head 3 and roll body 3 are my creative implementations of the animations.
The security light hasn't been implemented and therefore the class 'SecurityLight.java' is redundant, and there is no button on the user interface to turn the security light on/off because of this.

The textures used are referenced as follows :
'alienarmtexture.jpg' - https://www.heeboklee.com/projects/1350_hulk_08 - Accessed 23rd November 2023
'alienheadtexture.jpg' - https://anjumiah.wordpress.com/2012/03/21/textures-for-alien/ - Accessed 23rd November 2023
'alientexture.jpg' - https://www.filterforge.com/filters/7168.html - Accessed 23rd November 2023
'alien2armtexture.jpg' - https://www.deviantart.com/fractalcaleidoscope/art/texture-fluid-alien-metal-848473168 - Accessed 1st December 2023
'alien2headtexture.jpg' - https://seamless-pixels.blogspot.com/2012/08/reptile-frog-snake-alien-skin-texture.html - Accessed 1st December 2023
'alien2texture.jpg' - https://www.pinterest.co.uk/pin/art-game-textures--13018286396489874/ - Accessed 1st December 2023
'sidewall.jpg' - https://www.wallpaperflare.com/house-surrounded-by-pine-trees-at-nighttime-snow-winter-mountains-wallpaper-zbzzh - Accessed 16th November 2023
'background.jpg' - https://wallpapercave.com/w/wp3365356 - Accessed 16th November 2023
'snowfloor.jpg' - https://www.vecteezy.com/video/2571463-texture-of-fresh-winter-snow-on-ground - Accessed 20th November 2023
'snowfalling.jpg' - https://www.storyblocks.com/video/stock/animation-of-snow-falling-production-quality-footage-in-prores-4444-codec-with-alpha-channel-rbzctfrtmjez7k30m - Accessed 22nd November 2023
'securitypost.jpg' - https://wallpapers.com/metal-texture-background - Accessed 4th December 2023