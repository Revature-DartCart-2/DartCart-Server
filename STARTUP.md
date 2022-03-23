## Startup API

### Running the application locally

[DartCart-Server/README.md at main · Revature-DartCart-2/DartCart-Server · GitHub](https://github.com/Revature-DartCart-2/DartCart-Server/blob/main/README.md)

### Setting up Azure Web App

The way this work flow is requires that the Azure App Service is already created so it starts with that.

1. First step is is pretty much the same as this example https://docs.microsoft.com/en-us/azure/app-service/quickstart-java?tabs=javase&pivots=platform-linux. It has some requirements like having an Azure account and Azure subscription. The only real deviation from the example is to clone from your repo. Make sure to change the pricing plan to your need.
  
2. Download the Publish Profile which can be found on the overview of the Web App in Azure Portal.![net  Import publish profile from Azure web app into Visual Studio Code or  vs for mac  Stack Overflow](https://i.stack.imgur.com/oFVBv.png)
  
3. Go to the settings of the Github repo and go to the security section and select Secrets/Actions to set up a new repository secret. Click new repository secret and make sure to name the AZURE_WEBAPP_PUBLISH_PROFILE and paste your publish profile you got from before. Add the secret to the repo. ![image](https://user-images.githubusercontent.com/32827900/159562889-897a5da1-628c-4768-a460-1e3b429e920f.png) ![image](https://user-images.githubusercontent.com/32827900/159562964-d4644e2f-b6d8-4fcd-80d1-0372f218e3f3.png)


  
4. Ideally the repo should still have the azure-webapps-java-jar.yml which is located in .github/workflows/ . This means that you don't have to create the file yourself. This file is what's telling Github actions to create and deploy to Azure. You can change the values in the env section to alter the name and version of java being used. Now everything should be setup where any pushes to the main will cause the create and deploy to occur. ![image](https://user-images.githubusercontent.com/32827900/159563116-6d9f5235-876f-48a5-a58e-85d6271509e8.png)

  

if seeking more information then please refer to the comments on the azure-webapps-java-jar.yml. There are multiple links to documentation which should fill in some knowledge gaps. This is intended to just get you running.
