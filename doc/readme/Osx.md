Apple Osx
======================
##Operation not permitted
Boot in BigSur Recovery Mode (press and hold Command + R at startup) and type in the terminal

    csrutil disable
 , this is the "new BigSur SIP disabled" (from my current BigSur normal booting terminal) 

- The reboot back to your desktop and type csrutil authenticated-root status and see if you et disabled. You should now to be able to change your permission.

-  [osxdaily](https://osxdaily.com/2018/10/09/fix-operation-not-permitted-terminal-error-macos/)
##Files nascosti
    
    write com.apple.finder AppleShowAllFiles true; killall Finder