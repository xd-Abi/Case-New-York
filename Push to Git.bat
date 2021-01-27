echo off
color 3
cmd /c git add *
cmd /c git commit -m "-"
cmd /c git push origin master
pause
