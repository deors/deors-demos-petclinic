cd ..\..\petclinic-heroku\workspace
git remote add heroku git@heroku.com:petclinic-cloud.git
git checkout master
git pull origin
git push heroku
