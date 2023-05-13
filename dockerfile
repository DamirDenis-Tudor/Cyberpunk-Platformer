FROM damir2002/cyberpunk:latest

RUN rm -rf *

COPY out/production/Cyberpunk_Platformer .
COPY libs/sqlite-jdbc-3.7.2.jar .

CMD java -cp ".:sqlite-jdbc-3.7.2.jar" Main

