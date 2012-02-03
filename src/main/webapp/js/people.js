$(function () {
  var searchUsers = function () {
    var args = {userName:$("#users-search input[type=text]").val()};
    $('#users').load(People.urls.findUsers, args, function () {
    })
  };
  var searchGroups = function (name, userName) {
    var args = {
      groupName:$("#groups-search input[type=text]").val(),
      userName:$("#groups-search input[type=hidden]").val()
    };
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
  $("#people").on("click", ".user", function () {
    $(this).siblings().removeClass("selected");
    $(this).addClass("selected");
    $("#groups-search input[type=hidden]").val($(this).attr("id"));
    searchGroups();
  });

  // Init UI
  searchUsers();
  searchGroups();
});
