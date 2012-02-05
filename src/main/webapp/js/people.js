$(function () {
  var searchUsers = function () {
    var args = {userName:$("#users-search input[type=text]").val()};
    $("<div></div>").load(People.urls.findUsers, args, function () {
      $("#users .checkable:not(.checked)").parents("tr").remove();
      $(this).find("tr").filter(function() {
        var id = $(this).children("td")[0].id;
        return document.getElementById(id) == null;
      }).appendTo("#users");
    })
  };
  var searchGroups = function (name, userName) {
    var args = $("#groups-search").serialize();
    $('#groups').load(People.urls.findGroups, args, function () {
    })
  };

  //
  $("#users-search").on("keyup", "input[type=text]", function () {
    searchUsers();
  });

  //
  $("#groups-search").on("keyup", "input[type=text]", function () {
    searchGroups();
  });

  //
  var addUser = function() {
    $("<input name='userName' type='hidden'/>").appendTo("#groups-search").val($(this).attr("id"));
  };
  var removeUser = function() {
    $("#groups-search input[type=hidden][value=" + $(this).attr("id") + "]").remove();
  };

  $("#users").on("click", ".checkable:not(.checked)", function(e) {
    $(this).addClass("checked");
    $(this).parents(".user").addClass("selected").each(addUser);
    $("#users").find(".user.selected .checkable:not(.checked)").parents(".user").removeClass("selected").each(removeUser);
    searchGroups();
    e.stopPropagation();
    e.preventDefault();
  });
  $("#people").on("click", ".checkable.checked", function(e) {
    $(this).removeClass("checked");
    $(this).parents(".user").removeClass("selected").each(removeUser);
    searchGroups();
    e.stopPropagation();
    e.preventDefault();
  });

  //
  $("#people").on("click", ".user", function () {
    if ($("#users .checkable.checked").size() == 0)
    {
      $("#users .user.selected").removeClass("selected").each(removeUser);
      $(this).addClass("selected").each(addUser);
      searchGroups();
    }
  });

  // Init UI
  searchUsers();
  searchGroups();
});
