cd ..\..\petclinic-rhc\workspace
git remote add openshift ssh://355f7bd93f4144248e010ed4f0fb7665@petclinic-sdcassets.rhcloud.com/~/git/petclinic.git/
git checkout master
git pull origin
git push openshift
