test( "Expand with node-header Test", function() {

    customize = $( ".customize:first" );
    console.log("Button display",customize.css('display'))
    console.log("Button id",customize.attr('id'))

    ok (!customize.is(":visible"), "Should not find Customize button")

    treeNodeClick($(".tree-node-header" ).first());

    ok (customize.is(":visible"), "Should find Customize button")
    treeNodeClick($(".tree-node-header" ).first());
    ok (!customize.is(":visible"), "Should not find Customize button")

    /*    equal( 0, 0, "Zero; equal succeeds" );
        equal( "", 0, "Empty, Zero; equal succeeds" );
        equal( "", "", "Empty, Empty; equal succeeds" );
        equal( 0, 0, "Zero, Zero; equal succeeds" );

        equal( "three", 3, "Three, 3; equal fails" );
        equal( null, false, "null, false; equal fails" );*/
});

test("Grant node should enherit", function() {

    nodeGrantCheckbox = $( ".grant-node:first" );
    nodeGrantCheckbox.prop('checked', true);

    console.log("nodeGrantCheckbox id",nodeGrantCheckbox.attr('id'))
    grantNode(nodeGrantCheckbox);

    firstChildCheckBox = $( "#tree-container").find('.grant-node').eq(3)
    console.log("firstChildCheckBox id",firstChildCheckBox.attr('id'))

    ok (firstChildCheckBox.prop('checked'), 'firstChildCheckBox child should be checked');
    nodeGrantCheckbox.prop('checked', false);

    grantNode(nodeGrantCheckbox);
    ok (!firstChildCheckBox.prop('checked'), 'firstChildCheckBox child should be unchecked');

});

test("Row node-header click should select and not expand if set to Select", function() {
    document.getElementById("node-click-action").click();
    treeNodeClick($(".tree-node-header" ).first());
    nodeGrantCheckbox = $( ".grant-node:first" );
    checked = nodeGrantCheckbox.prop('checked');
    ok (checked, 'firstChildCheckBox child should be checked');

    document.getElementById("node-click-action").click();
    treeNodeClick($(".tree-node-header" ).first());
    checked = nodeGrantCheckbox.prop('checked');

    ok (checked, 'firstChildCheckBox child should be checked because we changed to expand behavior');
    document.getElementById("node-click-action").click();
    treeNodeClick($(".tree-node-header" ).first());
    checked = nodeGrantCheckbox.prop('checked');

    ok (!checked, 'firstChildCheckBox finally not be checked because behavior is select and we undid the first click by clicking again');
    document.getElementById("node-click-action").click();

});

test("Customize button should show options", function() {
    treeNodeClick($(".tree-node-header" ).first());
    customize = $( ".customize:first" );
    customize[0].click();
    crudCheckbox = $( ".create:first" );
    ok (crudCheckbox.is(":visible"), "create checkbox should be visible")
    treeNodeClick($(".tree-node-header" ).first());
    customize[0].click();
});

test("Customize options should be inherited if inheritance is activated", function() {
    useInheritance = $('#use-inheritance').prop('checked');
    console.log("Use inheritence",useInheritance)
    crudCheckbox = $( ".create:first");
    crudCheckbox[0].click();

    if (useInheritance) {
        childCreate = $( "#tree-container").find('.create').eq(1);

    }
    ok (childCreate.prop('checked'), "Child create checkbox should be checked")

    crudCheckbox[0].click();
    ok (!childCreate.prop('checked'), "Child create checkbox should be not be checked")

});

test("Customize options should not be inherited if inheritance is inactivated", function() {
    $('#use-inheritance').prop('checked', false);
    console.log("Use inheritence",useInheritance)
    crudCheckbox = $( ".create:first");

    crudCheckbox[0].click();
    ok (!childCreate.prop('checked'), "Child create checkbox should be not be checked")
    $('#use-inheritance').prop('checked', true);

});