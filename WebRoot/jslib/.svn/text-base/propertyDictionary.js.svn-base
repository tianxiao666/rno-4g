selectCache = [];

//父属性名称,目标名称,父属性的SELECT ID,目标SELECT ID
function listNextDictionaryInfo(parentType, tagType, parentTagid, tagid,
		defaultval) {
	var key = parentType + "-" + $("#" + parentTagid).val();
	var dn = selectCache[key];
	$("#" + tagid).empty();
	if (dn == null) {
		$("#" + tagid).append('<option value="undefine">...</option>');
	} else {
		listDictionaryInfo(dn, tagType, tagid, defaultval);
	}
}

function listDictionaryInfo(dn, tagtype, tagid, defaultval) {
	dn.replace("$", " ");
	var data = {
		"dn" : dn,
		"propertyName" : tagtype
	};
	$.ajax( {
		type : 'post',
		url : "listNextLevelPropertyEntryInfo",
		data : data,
		dataType : 'text',
		success : function(data, textStatus) {
			var resultCollection = jQuery.parseJSON(data);
			$.each(resultCollection, function(key, object) {
				var val = object["value"].replace(" ", "$");
				$("#" + tagid).append(
						"<option value=" + val + ">" + object["name"]
								+ "</option>");
				selectCache[tagtype + "-" + val] = tagtype + "-" + val + ","
						+ dn;
				//				alert(tagtype+"-"+val+":-->"+tagtype+"-"+val+","+dn);
				});
			if (resultCollection.length <= 0) {
				$("#" + tagid).append('<option value="undefine">...</option>');
			}
			if (defaultval != null) {
				$("#" + tagid + " option[value=" + defaultval + "]").attr("selected",
						"selected");
			}
			$("#" + tagid).change();
		},
		failure : function(data, testStatus) {
		}
	});
}

function listDictionaryInfoOnly(dn, tagtype,successCallback) {
	dn.replace("$", " ");
	var data = {
		"dn" : dn,
		"propertyName" : tagtype
	};
	$.ajax( {
		type : 'post',
		url : "listNextLevelPropertyEntryInfo",
		data : data,
		dataType : 'text',
		success : function(data, textStatus) {
			var resultCollection = jQuery.parseJSON(data);
			var resultSet = [];
			$.each(resultCollection, function(key, object) {
				var val = object["value"];
				resultSet.push(val);
			});
			successCallback(resultSet);
		},
		failure : function(data, testStatus) {
		}
	});
}