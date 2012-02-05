$(function () {

  function log(str) {
    if (typeof(console) !== "undefined") {
      console.log(str);
    }
  }

  // The search user will initiate the scroller once
  var scrolling = false;

  //
  var searchUsers = function (append) {
    append == append || false;
    var args = $("#users-search").serialize();
    if (append) {
      var offset = $(".user").size();
      args = args + "&offset=" + offset;
    }
    log("doing request " + args)
    $("<div></div>").load(People.urls.findUsers, args, function () {
      if (!append) {
        $("#users .checkable:not(.checked)").parents(".user").remove();
      }
      $(this).find(".user").filter(function() {
        var id = this.id;
        return document.getElementById(id) == null;
      }).appendTo("#users");

      //
      if (!scrolling) {
        scrolling = true;
        scroller();
      }
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

  // Scroller defined before we call searchUsers below
  scroller = function() {
    $("#users").each(function() {
      var scrollBox= $(this).parents(".scrollbox").get(0);
      var scrolltop = scrollBox.scrollTop;
      var scrollheight = scrollBox.scrollHeight;
      var windowheight = scrollBox.clientHeight;
       var scrolloffset = 20;
      if(scrolltop >= (scrollheight - (windowheight + scrolloffset))) {
        searchUsers(true);
      }
    });
    setTimeout("scroller();", 1500);
  };

  // Init UI
  searchUsers();
  searchGroups();
});
