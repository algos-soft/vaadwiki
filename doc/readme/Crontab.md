Crontab
======================
###Formato

   
    * * * * *  command to execute
    │ │ │ │ │
    │ │ │ │ └─── day of week (0 - 6) (0 to 6 are Sunday to Saturday, or use names; 7 is Sunday, the same as 0)
    │ │ │ └──────── month (1 - 12)
    │ │ └───────────── day of month (1 - 31)
    │ └────────────────── hour (0 - 23)
    └─────────────────────── min (0 - 59)
    
![](/Users/gac/Documents/IdeaProjects/Operativi/Vaadflow14/doc/crontab.png)
- Any of these fields can be set to an asterisk (*), which stands for "first through last". For instance, to run a job every month, put * in the Month field.

- Ranges of numbers are allowed. Ranges are two numbers separated with a hyphen. The specified range is inclusive; for example, 8-11 for an "hours" entry specifies execution at hours 8, 9, 10 and 11.

- Lists are allowed. A list is a set of numbers (or ranges) separated by commas. Examples: "1,2,5,9", "0-4,8-12".

- Step values can be used in conjunction with ranges. For example, "0-23/2" in the Hours field means "every other hour." Steps are also permitted after an asterisk, so if you want to say "every two hours", you can use "*/2".

- Names can also be used for the "month" and "day of week" fields. Use the first three letters of the particular day or month (case doesn't matter). Ranges or lists of names are not allowed.

- Note that the day of a command's execution can be specified by two fields: day of month, and day of week. If both fields are restricted (in other words, they aren't *), the command will be run when either field matches the current time. For example, "30 4 1,15 * 5" would cause a command to be run at 4:30 A.M. on the 1st and 15th of each month, plus every Friday.
###Comandi da terminale
- Lista dei comandi attuali:
   
   
    crontab -l
- Apertura editor nano
    
    
    env EDITOR=nano crontab -e
- Script di startup
    
    
    4 * * * * /Users/gac/Desktop/wiki/startup.sh
- Script di shutdown
    
    
    6 * * * * /Users/gac/Desktop/wiki/shutdown.sh
- Uscita da editor nano

![](/Users/gac/Documents/IdeaProjects/Operativi/Vaadflow14/doc/shortcuts.png)

    CTRL+O per salvare
    CTRL+X per uscire
###Mac osx
- Alternativa per Mac
- Creating MacOS startup jobs with launchd [alvinalexander](https://alvinalexander.com/mac-os-x/mac-osx-startup-crontab-launchd-jobs/)    

###Examples
- To run /usr/bin/sample.sh at 12.59 every day and suppress the output
    
    
    59 12 * * * simon /usr/bin/sample.sh > /dev/null 2>&1

- To run sample.sh everyday at 9pm (21:00)
    
    
    0 21 * * * sample.sh 1>/dev/null 2>&1

- To run sample.sh every Tuesday to Saturday at 1am (01:00)
    
    
    0 1 * * 2-7 sample.sh 1>/dev/null 2>&1

- To run sample.sh at 07:30, 09:30 13:30 and 15:30

    
    30 07,09,13,15 * * * sample.sh

- To run sample.sh at 07:30, 09:30 13:30 and 15:30

    
    30 07,09,13,15 * * * sample.sh

- To run sample.sh at 2am daily.

    
    0 2 * * *  sample.sh ( This is widely used in cases of backup of files/databases etc on daily basis. )

- To run sample.sh twice a day. say 5am and 5pm

    
    0 5,17 * * * sample.sh

- To run sample.sh every minutes.

    
    * * * * * sample.sh

- To run sample.sh every Sunday at 5 PM.

    
    
    0 17 * * sun  sample.sh

- To run sample.sh every 10 minutes.

    
    */10 * * * * sample.sh

- To run sample.sh on selected months.

    
    * * * jan,may,aug *  sample.sh

- To run sample.sh selected days.

    
    0 17 * * sun,fri  sample.sh

- To run sample.sh first sunday of every month.

    
    0 2 * * sun  [ $(date +%d) -le 07 ] && sample.sh

- To run sample.sh every four hours.

    
    0 */4 * * * sample.sh

- To run sample.sh every Sunday and Monday.

    
    0 4,17 * * sun,mon sample.sh

- To run sample.sh every 30 Seconds.


    * * * * * sample.sh
    * * * * *  sleep 30; sample.sh

- To run multiple jobs using single cron.

    
    * * * * * sample1.sh; sample2.sh

- To run sample.sh on yearly ( @yearly ).


    @yearly sample.sh

- To run sample.sh on monthly ( @monthly ).

    
    @monthly sample.sh

- To run sample.sh on Weekly ( @weekly ).

    
    @weekly sample.sh

- To run sample.sh on daily ( @daily ).

    
    @daily sample.sh

- To run sample.sh on hourly ( @hourly ).

    
    @hourly sample.sh

- To run sample.sh on system reboot ( @reboot ).

    @reboot sample.sh

###Link    
- Schedule jobs [michelsen](https://ole.michelsen.dk/blog/schedule-jobs-with-crontab-on-mac-osx/)
- Scheduling jobs [medium](https://medium.com/better-programming/https-medium-com-ratik96-scheduling-jobs-with-crontab-on-macos-add5a8b26c30)
- Linux crontab command [computerhope](https://www.computerhope.com/unix/ucrontab.htm)