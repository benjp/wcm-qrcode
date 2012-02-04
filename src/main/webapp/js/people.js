$(function () {
  var searchUsers = function () {
    var args = {userName:$("#users-search input[type=text]").val()};
    $('#users').load(People.urls.findUsers, args, function () {
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
  $("#people").on("click", ".user", function () {
    if ($("#users input[type='checkbox']:checked").size() == 0)
    {
      $("#users .user.selected input[type='checkbox']:not(:checked)").parents(".user").removeClass("selected");
      $(this).addClass("selected");
      $("#groups-search input[type=hidden]").remove();
      $("<input name='userName' type='hidden'/>").appendTo("#groups-search").val($(this).attr("id"));
      searchGroups();
    }
  });
  $("#people").on("click", ".user input[type='checkbox']", function(e) {
    if ($(this).is (":checked")) {
      $("#users .user.selected input[type='checkbox']:not(:checked)").parents(".user").removeClass("selected").each(function() {
        $("#groups-search input[type=hidden][value=" + $(this).attr("id") + "]").remove();
      });
      $(this).parents(".user").addClass("selected");
      $("<input name='userName' type='hidden'/>").appendTo("#groups-search").val($(this).parents(".user").attr("id"));
    } else {
      var foo = $(this).parents(".user");
      $("#groups-search input[type=hidden][value=" + foo.attr("id") + "]").remove();
      foo.removeClass("selected");
    }
    searchGroups();
    e.stopPropagation();
  });

  // Init UI
  searchUsers();
  searchGroups();
});
