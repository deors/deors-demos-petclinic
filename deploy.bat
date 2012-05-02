cd %~dp0
git remote add heroku git@heroku.com:petclinic-titc.git
git checkout master
git pull origin
git push heroku
