package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaTeamApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.team.adapter.TeamProjectMemberAdapter;
import com.guoxiaoxing.kitty.team.bean.Team;
import com.guoxiaoxing.kitty.team.bean.TeamMember;
import com.guoxiaoxing.kitty.team.bean.TeamMemberList;
import com.guoxiaoxing.kitty.team.bean.TeamProject;
import com.guoxiaoxing.kitty.team.ui.TeamMainActivity;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 *
 * @author guoxiaoxing
 */
public class TeamProjectMemberFragment extends
	BaseListFragment<TeamMember> {

    private Team mTeam;

    private int mTeamId;

    private TeamProject mTeamProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle bundle = getArguments();
	if (bundle != null) {
	    mTeam = (Team) bundle
		    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
	    
	    mTeamProject = (TeamProject) bundle.getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);

	    mTeamId = mTeam.getId();
	}
    }

    @Override
    protected TeamProjectMemberAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamProjectMemberAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return "team_project_member_list_" + mTeamId + "_" + mTeamProject.getGit().getId();
    }

    @Override
    protected TeamMemberList parseList(InputStream is) throws Exception {
	TeamMemberList list = XmlUtils.toBean(
		TeamMemberList.class, is);
	return list;
    }

    @Override
    protected TeamMemberList readList(Serializable seri) {
	return ((TeamMemberList) seri);
    }

    @Override
    protected void sendRequestData() {
	// TODO Auto-generated method stub
	OSChinaTeamApi.getTeamProjectMemberList(mTeamId, mTeamProject,
		mHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(List<TeamMember> data) {
	// TODO Auto-generated method stub
	super.executeOnLoadDataSuccess(data);
	if (mAdapter.getData().isEmpty()) {
	    setNoProjectMember();
	}
	mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
    }

    private void setNoProjectMember() {
	mErrorLayout.setErrorType(EmptyLayout.NODATA);
	mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
	String str = getResources().getString(
		R.string.team_empty_project_member);
	mErrorLayout.setErrorMessage(str);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamMember teamMember = mAdapter.getItem(position);
	if (teamMember != null) {
	    UIHelper.showTeamMemberInfo(getActivity(), mTeamId, teamMember);
	}
    }
}
