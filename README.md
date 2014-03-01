#Visor

A small JSF permission framework based on Type Safe View configs from [Deltaspikes JSF Module][1]. To define permissions with Visor, simply use the definitions explained in deltaspikes documentation.

As you will notice / may know Deltaspikes JSF module is in turn integrated with Deltaspike Security and offers an integration point for third part security. This is where Visor hooks in.

Any `@Secured(PermissionAccessDecisionVoter.class)` will be managed by Visor. Other security hooks can still run in parallel with other voters that you may have configured.

##How Does Visor work?

This explanation will use a CRUD application with regular users and admin users as example.

Each page you secure with Visor (how is defined above) will be mapped to a `Permission`. These permissions are the central concept in Visor.

How users get authorized to login is out of scope for Visor. Likewise it's out of scope for Visor to know how users receive permissions. Visor just expects to get a list of permissions available to the user and then verify that it has a matching permission when the agent navigates or ie. uses CRUD operations. That page-to-page validation simply uses Deltaspikes DecisionVoter concept. The only difference between admins and users in the eyes of Visor is that admins have 20 permissions while regular users only have 10 etc. To summarize Visor will *help you* assign permissions but after that it must be tracked and managed (ie persisted) by you. Don't worry visor will help you serialize the Permission to json so it's easy to ie. persist. 

So the normal flow is:

 - User logs in
 - Business logic unknown to Visor fetches the Permissions available to the user as Strings.
 - Logic unknown to visor exposes these permissions by implementing `PermissionDataHolder`. Typically this a `@SessionScoped` object.
 - Visor will match Permissions exposed by `PermissionDataHolder` with required permissions.

As mentioned before Page-to-page navigation is secured by Visor exactly as described in Deltaspike with no enhancements. When security stops the user from loading a page a `AccessDecisionVoteEvent` will be sent using the CDI event model.

Visor is also great for fine grained security on the actual page. When permissions are assigned CRUD is built in. In our case users can have access to x pages. On these pages the user may also have rights to create, read, update, delete or perhaps all four. This is defined using `disabled` and `rendered` from JSF:

The page is secured by using:

    @Inject
    PermissionContext permissionContext; 

    `boolean hasCreate = permissionContext.hasCreate();`

Or if by referencing it directly with EL `#{permissionContext.create}`

Visor will know the current page (and active permission) during runtime thus enabling a quite 

Sometimes one boolean for each operation in CRUD is not enough to describe everything that is allowed on a page. In such cases you can simply define parts directly on the the Type Safe View Configuration.

    public class Admin implements ViewConfig, SecuredPages { 
       public class PartOne implements PartPermission {}
    }

Above definition will create a Part Permission for the page Admin. The part permission will in turn be divided in CRUD as well. Use PartPermissions like this: `#{permissionContext.hasCreate('partParentSimpleClassNamePartSimpleClassName')}`

##To conclude

 - One master permission per page (mapped 1:1 to views secured with Visor)
 - Each master permission have built in sub permissions that represent create, read, update, delete respectivly.
 - Possible to have x number of part permissions per master permission. They inherit the view they are protecting but have their on sub permissions.  

  [1]: http://deltaspike.apache.org/jsf.html
