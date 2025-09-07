# DisciFi (Disciplined Finance)
your money’s in good hands - daddy’s here.

this is the spring boot back-end for DisciFi. the application helps you track your finances and also roasts you over your bad financial decisions as well as praises & motivates you over your good financial endeavours.

purpose:
- track finances with multiple accounts and customizable budgets.
- surface data and collect user inputs.
- generate insights using AI - both auto and on-demand.
- helps user build financial discipline via feedback loops and nudges.

> note: this is a rough base level idea of what the application offers. may modify things over the course of further development.

features (completed):
1. authentication: jwt-based authentication with session persistence.
2. all the necessary endpoints to mess with your finances.
3. auto insight generations (periodical).
4. soft-delete accounts with periodical deletions.
5. caching - using redis, currently limited - have to adapt wherever necessary.
6. queueing - using rabbit mq as the broker - since the llm calls are time-taking.
7. search - using elasticsearch - have to improve further to improve searches.

features (in-progress):
- [ ] on-demand insights using custom set of transactions.
- [ ] adapt caching to various fields.
- [ ] create a pipeline to support word-cloud (gives users a nuance of their expenses).
- [ ] various searching mechanisms (including but not limited to vector search, phonetic search, etc).
- [ ] mcp integration with google genai toolbox (compatible, makes the flow easier).
- [ ] pro-active assistance - nothing but category suggestions, etc. (ig it only applies to categories, can't think of any other).

the application uses _maven_ for dependency management.

setup:

edit the applications.properties accordingly.
```
the required instances are: (use docker for everything)
1. postgres 17.5
2. redis 7
3. rabbitmq 3
4. elasticsearch 9.1.3

you can check the compatibility with spring boot version and apply those versions.
here, i have mentioned the ones i use while developing. 
```

you can clone the repo and directly run the application using:
```
./mvnw spring-boot:run
```
servers runs at http://localhost:8080
