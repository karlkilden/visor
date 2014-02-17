#Visor

A small JSF security framework based on Type Safe View configs from [Deltaspikes JSF Module][1]. To use visor simply use the definitions explained in deltaspikes documentation.

As you will notice Deltaspikes JSF module is in turn integrated with Deltaspike Security and offers an integration point for third part security. This is where Visor hooks in.

Any `@Secured(PermissionAccessDecisionVoter.class)` will be managed by Visor. Other security hooks can still run in parallel with other voters configured.

**How Does Visors security work?**
This explanation will use a CRUD application with regular users and admin users as example.

Each page you secure with Visor (how is defined above) will be mapped to a `Permission`. These permissions are the central security concept in Visor.

How users get authorized to login is out of scope for Visor. Likewise it's out of scope for Visor to know how users receive permissions. Visor just expects to get a list of permissions available to the user and then verify that it has a matching permission when the agent navigates or ie. uses CRUD operations. The only difference between admins and users in the eyes of Visor is that admins have 20 permissions while regular users only have 10 etc.To summarize Visor will *help you* assign permissions but after that it must be tracked and managed (ie persisted) by you. Don't worry visor will help you serialize the Permission to a json String so it's easy to ie. persist. 

So the normal flow is:

 - User logs in
 - Business logic unknown to Visor fetches the Permissions available to the user.
 - If the Permissions have been serialized visor will assist and make sure they are Deserialized again. 
 - Logic unknown to visor exposes these permissions by implementing `PermissionHolder`. Typically this a `@SessionScoped` object.
 - Visor will match Permissions exposed by `PermissionHolder` with required permissions.

Page to page navigation is secured exactly as described in Deltaspike with no enhancements. The actual page however is secured by using:

    @Inject
    PermissionContext permissionContext; 

    permissionContext.hasCreate();

Or if by referencing it directly with EL `#{permissionContext.read}`

Sometimes one boolean for each operation in CRUD is not enough to describe everything that is allowed on a page. In such cases you can simply define parts directly on the the Type Safe View Configuration.

    public class Admin implements ViewConfig, SecuredPages { 
       public class PartOne implements PartPermission {}
    }

Above definition will create a Part Permission for the page Admin.


##To conclude

 - One master permission per page (mapped 1:1 to views secured with Visor)
 - Each master permission have built in sub permissions that represent create, read, update, delete respectivly.
 - Possible to have x number of part permissions per master permission. They inherit the view they are protecting but have their on sub permissions.  

  [1]: http://deltaspike.apache.org/jsf.html
