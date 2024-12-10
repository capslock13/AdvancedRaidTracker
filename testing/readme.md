# Testing Advanced Raid Tracker

Download IntelliJ from this link: https://www.jetbrains.com/idea/download/?section=windows

Make sure to Install the Community version and not Ultimate:

![](resources/1.png)

Once you install intelliJ you should see this window, press get from VCS:

![](resources/2.jpg)

Add this link to the 'URL' box and hit clone: https://github.com/Marco648135/cTimers

![](resources/3.jpg)

If it asks you to trust the project select trust:

![](resources/4.jpg)

Select which branch you're using, expand the 'remote' menu option:

![](resources/5.jpg)

Select advanced-features -> checkout

![](resources/6.jpg)

Expand the directory tree all the way to 'src/test/java/com/advancedraidtracker/AdvancedRaidTrackerTest.java' and right click and hit run

IF THERE IS NO RUN OPTION YOU HAVE TO WAIT FOR THE PROJECT TO FINISH INDEXING. THIS PROGRESS CAN BE SEEN IN THE PROGRESS BAR AT THE BOTTOM OF THE PROGRAM. IT CAN TAKE A FEW MINUTES.

![](resources/7.jpg)

It will fail with this message:

![](resources/8.jpg)

Expand the menu:

![](resources/9.jpg)

go to run->edit configurations

![](resources/10.jpg)

expand 'modify options'

![](resources/11.jpg)

select 'add vm options'

![](resources/12.jpg)

add '-ea' to the vm options box and '--developer-mode' to the program arguments then hit apply

![](resources/13.jpg)

Now hit the run button and the plugin should launch in runelite

![](resources/14.jpg)

I push changes to the working branch pretty frequently, ~once per day. You can update to the latest changes at any point by going to git->update project in the menu

![](resources/15.jpg)
